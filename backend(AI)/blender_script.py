
import bpy

# �� �� �ʱ�ȭ
bpy.ops.wm.read_factory_settings(use_empty=True)

# ���� ���� - ũ��� 1x1x0.05�� ��� �ڽ��� ����
bpy.ops.mesh.primitive_cube_add(size=1, enter_editmode=False, location=(0, 0, 0))
frame = bpy.context.object
frame.scale = (1.2, 1.6, 0.05)  # ������ ũ�⸦ ����

# �̹��� �ؽ�ó�� ����� ��� ���� (���� �ȿ� �� �׸�)
bpy.ops.mesh.primitive_plane_add(size=1, enter_editmode=False, align='WORLD', location=(0, 0, 0.06))
canvas = bpy.context.object
canvas.scale = (1.0, 1.4, 1)

# �̹��� �ؽ�ó �߰�
image_path = r"C:\Users\SSAFY\Desktop\wikiart\Baroque\adriaen-brouwer_a-boor-asleep.jpg"
image = bpy.data.images.load(image_path)
texture = bpy.data.textures.new("ArtworkTexture", type='IMAGE')
texture.image = image

# ��Ƽ���� ���� �� �ؽ�ó ����
material = bpy.data.materials.new(name="ArtworkMaterial")
material.use_nodes = True
bsdf = material.node_tree.nodes["Principled BSDF"]
texImage = material.node_tree.nodes.new('ShaderNodeTexImage')
texImage.image = image
material.node_tree.links.new(bsdf.inputs['Base Color'], texImage.outputs['Color'])

# ��鿡 ��Ƽ���� ����
canvas.data.materials.append(material)

# �� ī�޶� �߰�
bpy.ops.object.camera_add(location=(0, -3, 1), rotation=(1.1, 0, 0))
camera = bpy.context.object

# GLB ���Ϸ� ����
bpy.ops.export_scene.gltf(filepath="ar_image\92c570fb-2cfa-4b06-97cf-50690b36f8ac.glb", export_format='GLB')
