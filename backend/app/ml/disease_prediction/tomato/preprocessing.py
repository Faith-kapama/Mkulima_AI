import io
import base64

def preprocess_tomato_image(b64_string: str, target_size=(224, 224)):
    try:
        from PIL import Image
        import numpy as np

        if "," in b64_string:
            b64_string = b64_string.split(",")[1]

        image_bytes = base64.b64decode(b64_string)
        image = Image.open(io.BytesIO(image_bytes)).convert("RGB")
        image = image.resize(target_size)
        img_array = np.array(image, dtype=np.float32) / 255.0
        return np.expand_dims(img_array, axis=0)
    except Exception:
        return None
