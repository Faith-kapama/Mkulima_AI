import os
from typing import List
from pydantic_settings import BaseSettings

class Settings(BaseSettings):
    PROJECT_NAME: str = "MkulimaAI Backend"
    ENVIRONMENT: str = "development"
    DEBUG: bool = True
    SECRET_KEY: str = "mkulima_super_secret_jwt_key_tz_2026_secure"
    ACCESS_TOKEN_EXPIRE_MINUTES: int = 10080
    ALGORITHM: str = "HS256"

    DATABASE_URL: str = "sqlite:///./mkulima.db"
    
    HOST: str = "0.0.0.0"
    PORT: int = 8000
    CORS_ORIGINS: List[str] = ["*"]

    AFRICAS_TALKING_USERNAME: str = "sandbox"
    AFRICAS_TALKING_API_KEY: str = "your_africastalking_key"
    OPENWEATHER_API_KEY: str = "your_openweather_key"
    USSD_SERVICE_CODE: str = "*384*55#"
    MEDIA_DIR: str = os.path.join(os.path.dirname(os.path.dirname(os.path.dirname(__file__))), "uploads", "crop_images")

    class Config:
        env_file = ".env"
        extra = "allow"

settings = Settings()
