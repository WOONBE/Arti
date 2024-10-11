
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
image_path = r"C:\Users\SSAFY\Desktop\wikiart\Baroque\adriaen-brouwer_a-boor-asleep.jpg"
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
bpy.ops.export_scene.gltf(filepath="ar_image\92c570fb-2cfa-4b06-97cf-50690b36f8ac.glb", export_format='GLB')
