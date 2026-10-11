import strawberry
from typing import Optional
from datetime import datetime
from app.core.database import SessionLocal
from app.models.diagnosis import Diagnosis
from app.schemas.diagnosis import DiagnosisResultPayload, DiagnosisType
from app.services.disease_service import DiseasePredictionService

disease_service = DiseasePredictionService()

@strawberry.type
class DiagnosisMutations:

    @strawberry.mutation
    def submit_crop_diagnosis(
        self,
        crop: str,
        image_base64: str,
        symptoms: Optional[str] = None
    ) -> DiagnosisResultPayload:
        # Run ML inference
        prediction = disease_service.predict(crop=crop, image_base64=image_base64)
        
        db = SessionLocal()
        try:
            record = Diagnosis(
                crop=crop,
                detected_disease=prediction["detected_disease"],
                confidence_score=prediction["confidence_score"],
                severity=prediction["severity"],
                treatment_recommendations=prediction["treatment_recommendations"],
                prevention_tips=prediction["prevention_tips"],
                estimated_yield_impact_percent=prediction["estimated_yield_impact_percent"],
                created_at=datetime.utcnow()
            )
            db.add(record)
            db.commit()
            db.refresh(record)

            return DiagnosisResultPayload(
                success=True,
                message="Uchunguzi wa mmea umekamilika",
                diagnosis=DiagnosisType(
                    id=record.id,
                    crop=record.crop,
                    image_path=record.image_path,
                    detected_disease=record.detected_disease,
                    confidence_score=record.confidence_score,
                    severity=record.severity,
                    treatment_recommendations=record.treatment_recommendations,
                    prevention_tips=record.prevention_tips,
                    estimated_yield_impact_percent=record.estimated_yield_impact_percent,
                    created_at=record.created_at.strftime("%Y-%m-%d %H:%M")
                )
            )
        finally:
            db.close()
