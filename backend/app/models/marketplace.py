from sqlalchemy import Column, Integer, String, Float, Boolean, ForeignKey, DateTime, Text
from sqlalchemy.orm import relationship
from datetime import datetime
from app.core.database import Base

class Marketplace(Base):
    __tablename__ = "marketplace_items"

    id = Column(Integer, primary_key=True, index=True)
    seller_id = Column(Integer, ForeignKey("users.id"), nullable=True)
    title = Column(String(150), nullable=False)
    category = Column(String(50), default="Produce")
    price = Column(Float, nullable=False)
    unit = Column(String(30), default="kg")
    quantity = Column(Float, default=100.0)
    location = Column(String(100), default="Morogoro")
    seller_name = Column(String(100), default="Mkulima")
    seller_phone = Column(String(30), nullable=False)
    is_organic = Column(Boolean, default=True)
    image_url = Column(String(255), nullable=True)
    description = Column(Text, nullable=True)
    is_available = Column(Boolean, default=True)
    created_at = Column(DateTime, default=datetime.utcnow)

    seller = relationship("User", back_populates="marketplace_items")
