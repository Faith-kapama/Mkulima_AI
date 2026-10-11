class MarketPredictor:

    def predict_price_trend(self, crop: str, month: str) -> dict:
        trends = {
            "Maize": {"trend": "UP", "expected_change": "+6%", "forecast": 1250},
            "Tomato": {"trend": "DOWN", "expected_change": "-4%", "forecast": 1650},
            "Rice": {"trend": "STABLE", "expected_change": "+1%", "forecast": 2800}
        }
        return trends.get(crop, {"trend": "STABLE", "expected_change": "0%", "forecast": 1000})
