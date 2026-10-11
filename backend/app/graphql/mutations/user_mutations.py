import strawberry
from typing import Optional
from app.core.database import SessionLocal
from app.models.farmer import Farmer
from app.schemas.user import FarmerProfileType

@strawberry.type
class UserMutations:

    @strawberry.mutation
    def update_farmer_profile(
        self,
        user_id: int,
        region: Optional[str] = None,
        district: Optional[str] = None,
        farm_size_acres: Optional[float] = None,
        soil_type: Optional[str] = None,
        primary_crops: Optional[str] = None
    ) -> Optional[FarmerProfileType]:
        db = SessionLocal()
        try:
            farmer = db.query(Farmer).filter(Farmer.user_id == user_id).first()
            if not farmer:
                farmer = Farmer(user_id=user_id)
                db.add(farmer)
            
            if region is not None:
                farmer.region = region
            if district is not None:
                farmer.district = district
            if farm_size_acres is not None:
                farmer.farm_size_acres = farm_size_acres
            if soil_type is not None:
                farmer.soil_type = soil_type
            if primary_crops is not None:
                farmer.primary_crops = primary_crops
            
            db.commit()
            db.refresh(farmer)
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
