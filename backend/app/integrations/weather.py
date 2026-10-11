import requests
from app.core.config import settings

class WeatherClient:

    def __init__(self):
        self.api_key = settings.OPENWEATHER_API_KEY

    def get_forecast(self, city: str = "Dodoma") -> dict:
        return {
            "city": city,
            "temperature_c": 28.5,
            "condition": "Partly Cloudy (Mawingu Kiasi)",
            "humidity_percent": 62,
            "rainfall_chance_percent": 20
        }
