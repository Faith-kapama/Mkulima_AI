from app.core.database import SessionLocal, engine, Base
from app.models.market_price import MarketPrice
from app.models.crop import Crop
from app.models.disease import Disease
from app.models.marketplace import Marketplace

def seed():
    Base.metadata.create_all(bind=engine)
    db = SessionLocal()
    try:
        # Check if already seeded
        if db.query(MarketPrice).count() == 0:
            prices = [
                MarketPrice(crop_name="Maize (Mahindi)", category="Cereals", market_location="Kariakoo, DSM", price_per_unit=1200.0, unit="kg", price_trend="UP"),
                MarketPrice(crop_name="Tomatoes (Nyanya)", category="Horticulture", market_location="Mwanjelwa, Mbeya", price_per_unit=1800.0, unit="kg", price_trend="DOWN"),
                MarketPrice(crop_name="Onions (Vitunguu)", category="Horticulture", market_location="Tandale, DSM", price_per_unit=2500.0, unit="kg", price_trend="UP"),
                MarketPrice(crop_name="Rice (Mchele)", category="Cereals", market_location="Mbeya Town", price_per_unit=2800.0, unit="kg", price_trend="STABLE"),
                MarketPrice(crop_name="Beans (Maharage)", category="Legumes", market_location="Arusha Kuu", price_per_unit=3200.0, unit="kg", price_trend="STABLE"),
            ]
            db.add_all(prices)

        if db.query(Crop).count() == 0:
            crops = [
                Crop(name="Maize", swahili_name="Mahindi", category="Cereal", harvest_cycle_days=120),
                Crop(name="Tomato", swahili_name="Nyanya", category="Horticulture", harvest_cycle_days=90),
                Crop(name="Beans", swahili_name="Maharage", category="Legumes", harvest_cycle_days=75),
                Crop(name="Rice", swahili_name="Mpunga", category="Cereal", harvest_cycle_days=140),
            ]
            db.add_all(crops)

        db.commit()
        print("Database seeded successfully with Tanzanian agricultural market and crop data!")
    finally:
        db.close()

if __name__ == "__main__":
    seed()
