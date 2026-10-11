from app.ml.disease_prediction.maize.predictor import MaizeDiseasePredictor

def test_maize_predictor_heuristic():
    predictor = MaizeDiseasePredictor()
    result = predictor.predict("dummy_base64_string")
    assert result["crop"] == "Maize"
    assert "detected_disease" in result
    assert result["confidence_score"] > 0.5
