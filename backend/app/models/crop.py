from sqlalchemy import Column, Integer, String, Text, Float
from app.core.database import Base

class Crop(Base):
    __tablename__ = "crops"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(80), unique=True, nullable=False) # Maize, Tomato, Rice, Beans
    swahili_name = Column(String(80), nullable=False)     # Mahindi, Nyanya, Mpunga, Maharage
    category = Column(String(50), default="Cereal")
    optimal_temp_min = Column(Float, default=18.0)
    optimal_temp_max = Column(Float, default=32.0)
    optimal_rainfall_mm = Column(Float, default=800.0)
    harvest_cycle_days = Column(Integer, default=120)
    description = Column(Text, nullable=True)
