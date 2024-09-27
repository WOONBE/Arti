from sqlalchemy.orm import Session
from config.models import Gallery
from config.models import Audio


from transformers import pipeline
from transformers import AutoProcessor, MusicgenForConditionalGeneration
import os
import scipy



def generation_music(gallery_id, db:Session):
    try:
        image_path = db.query(Gallery).filter(Gallery.gallery_id == gallery_id).first()

        #image2text
        captioner = pipeline("image-to-text", model = "Salesforce/blip-image-captioning-base")
        # result = captioner(os.path.join("C:/Users/SSAFY/Desktop/wikiart", image_path.gallery_img))
        result = captioner(os.path.join('https://j11d106.p.ssafy.io/static/', image_path.gallery_img))

        # text2music
        processor = AutoProcessor.from_pretrained("facebook/musicgen-small")
        model = MusicgenForConditionalGeneration.from_pretrained("facebook/musicgen-small")

        inputs = processor(
        text=[result[0]['generated_text'], 'jazz with piano and cello'],
        padding=True,
        return_tensors="pt",
        )

        audio_values = model.generate(**inputs, guidance_scale=3, max_new_tokens=1800)

        audio_data = audio_values[0, 0].detach().cpu().numpy()  # Convert to NumPy array

        sampling_rate = model.config.audio_encoder.sampling_rate

        # Calculate number of samples to trim (5 seconds worth of samples)
        samples_to_trim = 6 * sampling_rate  # 5 seconds
        if audio_data.shape[0] > samples_to_trim:
            audio_data = audio_data[:-int(samples_to_trim)]  # Trim the last 5 seconds


        audio_save_directory = '/music/audio'
        os.makedirs(audio_save_directory, exist_ok=True)
        wav_file_name = f"generated_audio_{gallery_id}.wav"
        wav_file_path = os.path.join(audio_save_directory, wav_file_name).replace('\\', '/')

        sampling_rate = model.config.audio_encoder.sampling_rate
        scipy.io.wavfile.write(wav_file_path, rate=sampling_rate, data=audio_data)

        new_audio = Audio(audio_path=wav_file_path, gallery_id=gallery_id)
        db.add(new_audio)
        db.commit()

        result = os.path.join('http://j11d106.p.ssafy.io:9000/music/', wav_file_path)

        return result

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


def show_music_base(music_path):
    return