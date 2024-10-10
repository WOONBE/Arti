from fastapi import APIRouter, UploadFile, File
from fastapi.responses import FileResponse
from pygltflib import GLTF2, Texture, Image as GLTFImage, Material, Primitive, Mesh, Node, Scene
from PIL import Image as PILImage
import subprocess
import os
import uuid

router = APIRouter(prefix='/ar')

# 파일 저장 경로 설정
UPLOAD_DIR = "uploaded_images"
GLB_OUTPUT_DIR = "ar_image"

# 디렉토리 생성
if not os.path.exists(UPLOAD_DIR):
    os.makedirs(UPLOAD_DIR)

if not os.path.exists(GLB_OUTPUT_DIR):
    os.makedirs(GLB_OUTPUT_DIR)

# Blender를 사용하여 GLB 파일 생성
def create_glb_with_blender(image_path, glb_output_path):
    blender_script = f"""
import bpy

# 새 씬 초기화
bpy.ops.wm.read_factory_settings(use_empty=True)

# 액자 생성 - 크기는 1x1x0.05의 평면 박스로 생성
bpy.ops.mesh.primitive_cube_add(size=1, enter_editmode=False, location=(0, 0, 0))
frame = bpy.context.object
frame.scale = (1.2, 1.6, 0.05)  # 액자의 크기를 조정

# 이미지 텍스처가 적용될 평면 생성 (액자 안에 들어갈 그림)
bpy.ops.mesh.primitive_plane_add(size=1, enter_editmode=False, align='WORLD', location=(0, 0, 0.06))
canvas = bpy.context.object
canvas.scale = (1.0, 1.4, 1)

# 이미지 텍스처 추가
image_path = r"{image_path}"
image = bpy.data.images.load(image_path)
texture = bpy.data.textures.new("ArtworkTexture", type='IMAGE')
texture.image = image

# 머티리얼 생성 및 텍스처 적용
material = bpy.data.materials.new(name="ArtworkMaterial")
material.use_nodes = True
bsdf = material.node_tree.nodes["Principled BSDF"]
texImage = material.node_tree.nodes.new('ShaderNodeTexImage')
texImage.image = image
material.node_tree.links.new(bsdf.inputs['Base Color'], texImage.outputs['Color'])

# 평면에 머티리얼 적용
canvas.data.materials.append(material)

# 씬 카메라 추가
bpy.ops.object.camera_add(location=(0, -3, 1), rotation=(1.1, 0, 0))
camera = bpy.context.object

# GLB 파일로 저장
bpy.ops.export_scene.gltf(filepath="{glb_output_path}", export_format='GLB')
"""
    # Blender 스크립트 파일 작성
    script_path = "blender_script.py"
    with open(script_path, "w") as script_file:
        script_file.write(blender_script)

    # Blender 백그라운드에서 실행하여 GLB 파일 생성
    # subprocess.run(["blender", "--background", "--python", script_path])
    blender_executable = r"C:\Program Files\Blender Foundation\Blender 4.2\blender.exe"
    # EC2에서 Blender 백그라운드에서 실행하여 GLB 파일 생성
    # blender_executable = r"/opt/blender/blender"


    # Blender 백그라운드에서 실행하여 GLB 파일 생성
    subprocess.run([blender_executable, "--background", "--python", script_path])

# 이미지 업로드 및 GLB 파일 생성 엔드포인트
@router.post("/upload-image/")
async def upload_image(image_path):
    # 이미지 파일 저장
    image_id = str(uuid.uuid4())
    # image_filename = f"{image_id}.png"
    # image_path = os.path.join(UPLOAD_DIR, image_filename)

    # with open(image_path, "wb") as image_file:
    #     content = await file.read()
    #     image_file.write(content)

    # GLB 파일 생성
    glb_filename = f"{image_id}.glb"
    glb_output_path = os.path.join(GLB_OUTPUT_DIR, glb_filename)

    create_glb_with_blender(image_path, glb_output_path)

    # GLB 파일 다운로드 링크 제공
    # return {"glb_file_url": f"/download-glb/{glb_filename}"}
    if os.path.exists(glb_output_path):
        return FileResponse(glb_output_path)
    return {"error": "File not found"}

# GLB 파일 다운로드 엔드포인트
@router.get("/download-glb/{filename}")
async def download_glb(filename: str):
    glb_file_path = os.path.join(GLB_OUTPUT_DIR, filename)
    if os.path.exists(glb_file_path):
        return FileResponse(glb_file_path)
    return {"error": "File not found"}
