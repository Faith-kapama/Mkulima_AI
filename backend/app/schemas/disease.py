import strawberry
from typing import Optional

@strawberry.type
class DiseaseType:
    id: int
    crop_name: str
    name: str
    swahili_name: Optional[str] = None
    symptoms: str
    causes: Optional[str] = None
    treatment_chemical: Optional[str] = None
    treatment_organic: Optional[str] = None
    prevention: Optional[str] = None
