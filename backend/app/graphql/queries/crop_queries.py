from typing import List, Optional
import strawberry
from app.core.database import SessionLocal
from app.models.crop import Crop
from app.schemas.crop import CropType

@strawberry.type
class CropQueries:

    @strawberry.field
    def all_crops(self) -> List[CropType]:
        db = SessionLocal()
        try:
            crops = db.query(Crop).all()
            return [
                CropType(
                    id=c.id,
                    name=c.name,
                    swahili_name=c.swahili_name,
                    category=c.category,
                    optimal_temp_min=c.optimal_temp_min,
                    optimal_temp_max=c.optimal_temp_max,
                    optimal_rainfall_mm=c.optimal_rainfall_mm,
                    harvest_cycle_days=c.harvest_cycle_days,
                    description=c.description
                ) for c in crops
            ]
        finally:
            db.close()
