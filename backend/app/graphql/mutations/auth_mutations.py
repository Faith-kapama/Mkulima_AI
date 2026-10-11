import strawberry
from typing import Optional
from app.core.database import SessionLocal
from app.models.user import User
from app.models.farmer import Farmer
from app.core.security import get_password_hash, verify_password, create_access_token

@strawberry.type
class AuthPayload:
    success: bool
    message: str
    token: Optional[str] = None
    user_id: Optional[int] = None
    full_name: Optional[str] = None

@strawberry.type
class AuthMutations:

    @strawberry.mutation
    def register_farmer(
        self,
        phone_number: str,
        full_name: str,
        password: str,
        region: str = "Dodoma",
        district: str = "Chamwino",
        farm_size_acres: float = 2.5,
        primary_crops: str = "Maize, Tomato"
    ) -> AuthPayload:
        db = SessionLocal()
        try:
            existing = db.query(User).filter(User.phone_number == phone_number).first()
            if existing:
                return AuthPayload(success=False, message="Mkulima mwenye namba hii tayari yupo")

            user = User(
                phone_number=phone_number,
                full_name=full_name,
                hashed_password=get_password_hash(password),
                role="FARMER"
            )
            db.add(user)
            db.commit()
            db.refresh(user)

            farmer = Farmer(
                user_id=user.id,
                region=region,
                district=district,
                farm_size_acres=farm_size_acres,
                primary_crops=primary_crops
            )
            db.add(farmer)
            db.commit()

            token = create_access_token(user.id)
            return AuthPayload(
                success=True,
                message="Usajili umefaulu kikamilifu",
                token=token,
                user_id=user.id,
                full_name=user.full_name
            )
        finally:
            db.close()

    @strawberry.mutation
    def login_farmer(self, phone_number: str, password: str) -> AuthPayload:
        db = SessionLocal()
        try:
            user = db.query(User).filter(User.phone_number == phone_number).first()
            if not user or not user.hashed_password:
                return AuthPayload(success=False, message="Namba ya simu au nenosiri sio sahihi")
            
            if not verify_password(password, user.hashed_password):
                return AuthPayload(success=False, message="Nenosiri sio sahihi")

            token = create_access_token(user.id)
            return AuthPayload(
                success=True,
                message="Umefanikiwa kuingia",
                token=token,
                user_id=user.id,
                full_name=user.full_name
            )
        finally:
            db.close()
