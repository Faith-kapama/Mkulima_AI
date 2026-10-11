from app.ml.disease_prediction.tomato.predictor import TomatoDiseasePredictor

def test_tomato_predictor_heuristic():
    predictor = TomatoDiseasePredictor()
    result = predictor.predict("dummy_base64_string")
    assert result["crop"] == "Tomato"
    assert "detected_disease" in result
    assert result["confidence_score"] > 0.5
