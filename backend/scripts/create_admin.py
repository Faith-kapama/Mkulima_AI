from app.models.user import User
from app.core.database import SessionLocal
from app.core.security import get_password_hash

def create_admin(phone="+255700000001", password="AdminMkulima2026!", full_name="Mkulima Admin"):
    db = SessionLocal()
    try:
        user = db.query(User).filter(User.phone_number == phone).first()
        if not user:
            user = User(
                phone_number=phone,
                full_name=full_name,
                hashed_password=get_password_hash(password),
                role="ADMIN",
                is_verified=True
            )
            db.add(user)
            db.commit()
            print(f"Admin created successfully with phone {phone}")
        else:
            print(f"Admin with phone {phone} already exists")
    finally:
        db.close()

if __name__ == "__main__":
    create_admin()
