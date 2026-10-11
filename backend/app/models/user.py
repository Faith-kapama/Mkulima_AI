from sqlalchemy import Column, Integer, String, Boolean, DateTime
from sqlalchemy.orm import relationship
from datetime import datetime
from app.core.database import Base

class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True)
    phone_number = Column(String(30), unique=True, index=True, nullable=False)
    email = Column(String(100), unique=True, index=True, nullable=True)
    hashed_password = Column(String(255), nullable=True)
    full_name = Column(String(120), nullable=False)
    is_active = Column(Boolean, default=True)
    is_verified = Column(Boolean, default=False)
    role = Column(String(30), default="FARMER") # FARMER, BUYER, AGRO_DEALER, ADMIN
    preferred_language = Column(String(10), default="sw") # sw, en
    created_at = Column(DateTime, default=datetime.utcnow)

    farmer_profile = relationship("Farmer", back_populates="user", uselist=False)
    diagnoses = relationship("Diagnosis", back_populates="user")
    marketplace_items = relationship("Marketplace", back_populates="seller")
