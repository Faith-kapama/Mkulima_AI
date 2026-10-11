from typing import List, Optional
import strawberry
from app.core.database import SessionLocal
from app.models.disease import Disease
from app.schemas.disease import DiseaseType

@strawberry.type
class DiseaseQueries:

    @strawberry.field
    def all_diseases(self, crop_name: Optional[str] = None) -> List[DiseaseType]:
        db = SessionLocal()
        try:
            query = db.query(Disease)
            if crop_name:
                query = query.filter(Disease.crop_name.ilike(f"%{crop_name}%"))
            diseases = query.all()
            return [
                DiseaseType(
                    id=d.id,
                    crop_name=d.crop_name,
                    name=d.name,
                    swahili_name=d.swahili_name,
                    symptoms=d.symptoms,
                    causes=d.causes,
                    treatment_chemical=d.treatment_chemical,
                    treatment_organic=d.treatment_organic,
                    prevention=d.prevention
                ) for d in diseases
            ]
        finally:
            db.close()
