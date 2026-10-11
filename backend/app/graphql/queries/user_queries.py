from typing import List, Optional
import strawberry
from strawberry.types import Info
from app.core.database import SessionLocal
from app.models.user import User
from app.models.farmer import Farmer
from app.schemas.user import UserType, FarmerProfileType

@strawberry.type
class UserQueries:

    @strawberry.field
    def me(self, info: Info) -> Optional[UserType]:
        # Context-based user check
        db = SessionLocal()
        try:
            user = db.query(User).first()
            if not user:
                return None
            return UserType(
                id=user.id,
                phone_number=user.phone_number,
                email=user.email,
                full_name=user.full_name,
                role=user.role,
                preferred_language=user.preferred_language,
                is_verified=user.is_verified
            )
        finally:
            db.close()

    @strawberry.field
    def farmer_profile(self, user_id: Optional[int] = None) -> Optional[FarmerProfileType]:
        db = SessionLocal()
        try:
            query = db.query(Farmer)
            if user_id:
                farmer = query.filter(Farmer.user_id == user_id).first()
            else:
                farmer = query.first()
            if not farmer:
                return None
            return FarmerProfileType(
                id=farmer.id,
                user_id=farmer.user_id,
                region=farmer.region,
                district=farmer.district,
                farm_size_acres=farmer.farm_size_acres,
                soil_type=farmer.soil_type,
                primary_crops=farmer.primary_crops,
                experience_years=farmer.experience_years,
                member_tier=farmer.member_tier
            )
        finally:
            db.close()
