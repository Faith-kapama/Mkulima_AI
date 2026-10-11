from sqlalchemy import Column, Integer, String, Text, DateTime
from datetime import datetime
from app.core.database import Base

class USSDSession(Base):
    __tablename__ = "ussd_sessions"

    id = Column(Integer, primary_key=True, index=True)
    session_id = Column(String(100), unique=True, index=True, nullable=False)
    phone_number = Column(String(30), nullable=False)
    network_code = Column(String(30), nullable=True)
    service_code = Column(String(30), nullable=True)
    current_step = Column(String(50), default="MAIN_MENU")
    session_data = Column(Text, default="{}")
    created_at = Column(DateTime, default=datetime.utcnow)
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)
