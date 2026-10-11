from app.services.disease_service import DiseasePredictionService

def test_specimen_verification_maize():
    service = DiseasePredictionService()
    img_data = "A" * 120
    result = service.diagnose(image_base64=img_data, crop="Maize")
    assert result["valid_specimen"] is True
    assert result["crop"] == "Maize"
    assert "Northern Corn Leaf Blight" in result["detected_disease"]
    assert result["confidence_score"] > 0.8

def test_specimen_verification_tomato():
    service = DiseasePredictionService()
    img_data = "A" * 120
    result = service.diagnose(image_base64=img_data, crop="Tomato")
    assert result["valid_specimen"] is True
    assert result["crop"] == "Tomato"
    assert "Tomato Early Blight" in result["detected_disease"]
    assert result["confidence_score"] > 0.8

def test_specimen_rejection_non_crop():
    service = DiseasePredictionService()
    invalid_data = "non_crop_object_" + "B" * 100
    result = service.diagnose(image_base64=invalid_data, crop="Maize")
    assert result["valid_specimen"] is False
    assert "error_message" in result
