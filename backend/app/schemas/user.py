import strawberry
from typing import Optional

@strawberry.type
class UserType:
    id: int
    phone_number: str
    email: Optional[str] = None
    full_name: str
    role: str
    preferred_language: str
    is_verified: bool

@strawberry.type
class FarmerProfileType:
    id: int
    user_id: int
    region: str
    district: str
    farm_size_acres: float
    soil_type: str
    primary_crops: str
    experience_years: int
    member_tier: str
