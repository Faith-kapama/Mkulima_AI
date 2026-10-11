from app.core.security import verify_password, get_password_hash, create_access_token, decode_access_token

def test_password_hashing():
    pwd = "secret_password"
    hashed = get_password_hash(pwd)
    assert verify_password(pwd, hashed) is True
    assert verify_password("wrong_password", hashed) is False

def test_jwt_token_flow():
    token = create_access_token(subject=42)
    sub = decode_access_token(token)
    assert sub == "42"
