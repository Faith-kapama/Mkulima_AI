from sqlalchemy import Column, Integer, String, Text, ForeignKey
from app.core.database import Base

class Disease(Base):
    __tablename__ = "diseases"

    id = Column(Integer, primary_key=True, index=True)
    crop_name = Column(String(80), nullable=False) # Maize, Tomato
    name = Column(String(120), nullable=False)      # Northern Corn Leaf Blight, Early Blight
    swahili_name = Column(String(120), nullable=True) # Ukungu wa majani ya mahindi
    symptoms = Column(Text, nullable=False)
    causes = Column(Text, nullable=True)
    treatment_chemical = Column(Text, nullable=True)
    treatment_organic = Column(Text, nullable=True)
    prevention = Column(Text, nullable=True)
