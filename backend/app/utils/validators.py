import re

def validate_tanzania_phone(phone: str) -> bool:
    # Validates +255 or 07 / 06 formats
    pattern = r"^(?:\+255|0)[67]\d{8}$"
    return bool(re.match(pattern, phone.strip()))
