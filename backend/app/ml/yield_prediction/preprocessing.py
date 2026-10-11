def preprocess_yield_features(acres: float, soil_type: str, rainfall: float) -> list:
    soil_map = {"Loamy": 1, "Clay": 2, "Sandy": 3, "Silt": 4}
    return [acres, soil_map.get(soil_type, 1), rainfall]
