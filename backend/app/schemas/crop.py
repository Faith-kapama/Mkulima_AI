import strawberry
from typing import Optional

@strawberry.type
class CropType:
    id: int
    name: str
    swahili_name: str
    category: str
    optimal_temp_min: float
    optimal_temp_max: float
    optimal_rainfall_mm: float
    harvest_cycle_days: int
    description: Optional[str] = None
