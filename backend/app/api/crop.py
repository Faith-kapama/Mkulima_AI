from fastapi import APIRouter, HTTPException, Depends
from pydantic import BaseModel, Field
from typing import Optional, List, Dict, Any
from app.services.disease_service import DiseasePredictionService
from app.ml.yield_prediction.predictor import YieldPredictor

router = APIRouter(prefix="/crop", tags=["Crop Intelligence & Diagnosis"])

disease_service = DiseasePredictionService()
yield_predictor = YieldPredictor()

class CropDiagnosisRequest(BaseModel):
    image_base64: str = Field(..., description="Base64 encoded string of the scanned leaf image")
    crop: Optional[str] = Field("Maize", description="Target crop: 'Maize' or 'Tomato'")

class CropDiagnosisResponse(BaseModel):
    valid_specimen: bool
    crop: str
    crop_sw: Optional[str] = None
    detected_disease: str
    detected_disease_sw: Optional[str] = None
    pathogen: Optional[str] = None
    pathogen_sw: Optional[str] = None
    confidence_score: float
    severity: str
    treatment_recommendations: str
    prevention_tips: str
    estimated_yield_impact_percent: float
    error_message: Optional[str] = None
    error_message_sw: Optional[str] = None

class MaizeYieldRequest(BaseModel):
    region: str = "Dodoma"
    season: str = "Long rains (Masika)"
    farm_area_ha: float = 2.0
    seed_type: str = "Improved"
    irrigation_used: bool = False
    fertilizer_used: bool = True
    fertilizer_rate_kg_ha: float = 100.0
    improved_seed_used: bool = True
    traditional_seed_used: bool = False
    recycled_seed_used: bool = False
    seed_quantity_kg_ha: float = 30.0
    planting_month: str = "December"
    rainfall_growing: Optional[float] = None
    temperature_c: Optional[float] = None
    soil_moisture_index: Optional[float] = None

@router.post("/diagnose", response_model=CropDiagnosisResponse)
def diagnose_crop_leaf(payload: CropDiagnosisRequest):
    """
    Diagnoses crop health from a scanned photo.
    Ensures that the scanned specimen is verified as Maize or Tomato.
    Rejects foreign objects and non-crop images.
    """
    result = disease_service.diagnose(
        image_base64=payload.image_base64,
        crop=payload.crop
    )
    return result

@router.post("/yield/predict")
def predict_maize_yield(payload: MaizeYieldRequest):
    """
    Predicts Maize yield using the 15-feature Tanzanian XGBoost regression model.
    Returns estimated yield in tonnes per hectare (t/ha).
    """
    result = yield_predictor.predict_xgboost_maize(
        region=payload.region,
        season=payload.season,
        farm_area_ha=payload.farm_area_ha,
        seed_type=payload.seed_type,
        irrigation_used=payload.irrigation_used,
        fertilizer_used=payload.fertilizer_used,
        fertilizer_rate_kg_ha=payload.fertilizer_rate_kg_ha,
        improved_seed_used=payload.improved_seed_used,
        traditional_seed_used=payload.traditional_seed_used,
        recycled_seed_used=payload.recycled_seed_used,
        seed_quantity_kg_ha=payload.seed_quantity_kg_ha,
        planting_month=payload.planting_month,
        rainfall_growing=payload.rainfall_growing,
        temperature_c=payload.temperature_c,
        soil_moisture_index=payload.soil_moisture_index
    )
    return result
