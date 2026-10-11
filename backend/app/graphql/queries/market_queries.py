from typing import List
import strawberry
from app.core.database import SessionLocal
from app.models.market_price import MarketPrice
from app.schemas.market import MarketPriceType

@strawberry.type
class MarketQueries:

    @strawberry.field
    def all_market_prices(self) -> List[MarketPriceType]:
        db = SessionLocal()
        try:
            prices = db.query(MarketPrice).all()
            if not prices:
                # Default Tanzania market data fallback
                return [
                    MarketPriceType(id=1, crop_name="Maize (Mahindi)", category="Cereals", market_location="Kariakoo, DSM", price_per_unit=1200.0, unit="kg", price_trend="UP", last_updated="Leo"),
                    MarketPriceType(id=2, crop_name="Tomatoes (Nyanya)", category="Horticulture", market_location="Mwanjelwa, Mbeya", price_per_unit=1800.0, unit="kg", price_trend="DOWN", last_updated="Leo"),
                    MarketPriceType(id=3, crop_name="Onions (Vitunguu)", category="Horticulture", market_location="Tandale, DSM", price_per_unit=2500.0, unit="kg", price_trend="UP", last_updated="Masaa 2 yaliyopita"),
                    MarketPriceType(id=4, crop_name="Rice (Mchele)", category="Cereals", market_location="Mbeya Town", price_per_unit=2800.0, unit="kg", price_trend="STABLE", last_updated="Leo"),
                    MarketPriceType(id=5, crop_name="Beans (Maharage)", category="Legumes", market_location="Arusha Kuu", price_per_unit=3200.0, unit="kg", price_trend="STABLE", last_updated="Jana"),
                ]
            return [
                MarketPriceType(
                    id=p.id,
                    crop_name=p.crop_name,
                    category=p.category,
                    market_location=p.market_location,
                    price_per_unit=p.price_per_unit,
                    unit=p.unit,
                    price_trend=p.price_trend,
                    last_updated=p.recorded_at.strftime("%Y-%m-%d")
                ) for p in prices
            ]
        finally:
            db.close()
