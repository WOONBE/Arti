from sqlalchemy.orm import Session
from config.models import Gallery
from config.models import Audio

import torch
from transformers import pipeline
from transformers import AutoProcessor, MusicgenForConditionalGeneration
import os
import scipy



captioner_model = None
musicgen_model = None
musicgen_processor = None

# GPU 사용 여부 확인
device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

# 모델을 애플리케이션 시작 시 한 번만 로드
def load_music_model():
    global captioner_model, musicgen_model, musicgen_processor
    
    # image-to-text 모델 로드
    captioner_model = pipeline("image-to-text", model="Salesforce/blip-image-captioning-base", device=0 if torch.cuda.is_available() else -1)

    # text-to-music 모델 로드
    musicgen_processor = AutoProcessor.from_pretrained("facebook/musicgen-small")
    musicgen_model = MusicgenForConditionalGeneration.from_pretrained("facebook/musicgen-small").to(device)

def generation_music(gallery_id: int, db: Session):
    try:
        # 갤러리 이미지 경로 불러오기
        image_path = db.query(Gallery).filter(Gallery.gallery_id == gallery_id).first()

        # image2text: 이미지 설명 생성
        result = captioner_model(os.path.join("C:/Users/SSAFY/Desktop/wikiart", image_path.gallery_img))

        inputs = musicgen_processor(
            text=[result[0]['generated_text'], 'piano and cello'],
            padding=True,
            return_tensors="pt",
        ).to(device)

        # 음악 생성
        audio_values = musicgen_model.generate(**inputs, guidance_scale=3, max_new_tokens=1800)

        # 오디오 데이터를 NumPy 배열로 변환
        audio_data = audio_values[0, 0].detach().cpu().numpy()

        # 샘플링 레이트 가져오기
        sampling_rate = musicgen_model.config.audio_encoder.sampling_rate

        # 6초 분량의 데이터를 잘라내기 (필요에 따라 조정 가능)
        samples_to_trim = 6 * sampling_rate
        if audio_data.shape[0] > samples_to_trim:
            audio_data = audio_data[:-int(samples_to_trim)]

        # 오디오 파일 저장
        audio_save_directory = '/audio'
        os.makedirs(audio_save_directory, exist_ok=True)
        wav_file_name = f"generated_audio_{gallery_id}.wav"
        wav_file_path = os.path.join(audio_save_directory, wav_file_name).replace('\\', '/')

        # WAV 파일로 오디오 저장
        scipy.io.wavfile.write(wav_file_path, rate=sampling_rate, data=audio_data)

        # 데이터베이스에 새로운 오디오 파일 경로 저장
        new_audio = Audio(audio_path=wav_file_path, gallery_id=gallery_id)
        db.add(new_audio)
        db.commit()

        return wav_file_path

    except Exception as e:
        db.rollback()
        raise e

    finally:
        db.close()

def delete_music_db(gallery_id, db:Session):
    data = db.query(Audio).filter(Audio.gallery_id == gallery_id).first()

    if data:
        db.delete(data)
        db.commit()
        return 'Susses'
    else:
        return "Not Exist"
    

def get_music(gallery_id, db : Session):
    data = db.query(Audio).filter(Audio.gallery_id == gallery_id).first()
    wav_file_path = os.path.join('http://j11d106.p.ssafy.io:9000/music/', data.audio_path)

    return wav_file_path


def show_music_base(gallery_id, db:Session):
    audio_file = db.query(Audio).filter(Audio.gallery_id == gallery_id).first()

    if not audio_file:
        return {"error": "Audio file not found"}

    file_path = audio_file.audio_path
    # 파일이 존재하는지 확인
    if not os.path.exists(file_path):
        return {"error": "File does not exist"}
    
    return file_path 