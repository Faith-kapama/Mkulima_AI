import strawberry
from typing import Optional

@strawberry.type
class DiagnosisType:
    id: int
    crop: str
    image_path: Optional[str] = None
    detected_disease: str
    confidence_score: float
    severity: str
    treatment_recommendations: Optional[str] = None
    prevention_tips: Optional[str] = None
    estimated_yield_impact_percent: float
    created_at: str

@strawberry.type
class DiagnosisResultPayload:
    success: bool
    message: str
    diagnosis: Optional[DiagnosisType] = None
