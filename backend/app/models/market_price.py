from sqlalchemy import Column, Integer, String, Float, DateTime
from datetime import datetime
from app.core.database import Base

class MarketPrice(Base):
    __tablename__ = "market_prices"

    id = Column(Integer, primary_key=True, index=True)
    crop_name = Column(String(80), nullable=False) # Maize, Tomatoes, Onions, Rice, Beans
    category = Column(String(50), default="Cereals")
    market_location = Column(String(100), nullable=False) # Kariakoo, DSM; Mwanjelwa, Mbeya
    price_per_unit = Column(Float, nullable=False)
    unit = Column(String(30), default="kg")
    price_trend = Column(String(20), default="UP") # UP, DOWN, STABLE
    recorded_at = Column(DateTime, default=datetime.utcnow)
