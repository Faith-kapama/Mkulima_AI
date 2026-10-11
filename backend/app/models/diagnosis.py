from sqlalchemy import Column, Integer, String, Float, Text, ForeignKey, DateTime
from sqlalchemy.orm import relationship
from datetime import datetime
from app.core.database import Base

class Diagnosis(Base):
    __tablename__ = "diagnoses"

    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, ForeignKey("users.id"), nullable=True)
    crop = Column(String(80), nullable=False)
    image_path = Column(String(255), nullable=True)
    detected_disease = Column(String(120), nullable=False)
    confidence_score = Column(Float, default=0.92)
    severity = Column(String(30), default="MEDIUM") # LOW, MEDIUM, HIGH
    treatment_recommendations = Column(Text, nullable=True)
    prevention_tips = Column(Text, nullable=True)
    estimated_yield_impact_percent = Column(Float, default=15.0)
    created_at = Column(DateTime, default=datetime.utcnow)

    user = relationship("User", back_populates="diagnoses")
