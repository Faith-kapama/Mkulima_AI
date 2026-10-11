from sqlalchemy import Column, Integer, String, Float, ForeignKey, DateTime
from sqlalchemy.orm import relationship
from datetime import datetime
from app.core.database import Base

class Farmer(Base):
    __tablename__ = "farmers"

    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, ForeignKey("users.id"), unique=True, nullable=False)
    region = Column(String(60), default="Morogoro")
    district = Column(String(60), default="Kilosa")
    ward = Column(String(60), nullable=True)
    farm_size_acres = Column(Float, default=2.5)
    soil_type = Column(String(50), default="Loamy") # Sandy, Clay, Loamy, Silt
    primary_crops = Column(String(200), default="Maize, Tomatoes, Beans")
    experience_years = Column(Integer, default=5)
    member_tier = Column(String(30), default="BRONZE") # BRONZE, SILVER, GOLD
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)

    user = relationship("User", back_populates="farmer_profile")
