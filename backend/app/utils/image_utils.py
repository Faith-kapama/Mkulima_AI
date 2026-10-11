import io
import base64
from PIL import Image

def save_base64_image(base64_str: str, file_path: str):
    if "," in base64_str:
        base64_str = base64_str.split(",")[1]
    image_data = base64.b64decode(base64_str)
    image = Image.open(io.BytesIO(image_data))
    image.save(file_path)
