import strawberry
from typing import Optional, List

@strawberry.type
class MarketPriceType:
    id: int
    crop_name: str
    category: str
    market_location: str
    price_per_unit: float
    unit: str
    price_trend: str
    last_updated: str

@strawberry.type
class YieldPredictionPayload:
    success: bool
    predicted_bags: float
    predicted_tons: float
    confidence_percent: float
    optimal_harvest_month: str
    recommendations: str
