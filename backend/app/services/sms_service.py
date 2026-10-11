from app.core.config import settings

class SMSService:

    def __init__(self):
        self.username = settings.AFRICAS_TALKING_USERNAME
        self.api_key = settings.AFRICAS_TALKING_API_KEY

    def send_sms(self, to_phone: str, message: str) -> bool:
        print(f"[SMS Gateway -> {to_phone}]: {message}")
        return True

    def send_market_alert(self, to_phone: str, crop: str, price: float, location: str):
        msg = f"MkulimaAI Bei: Bei ya {crop} katika soko la {location} sasa ni TSh {price:,.0f}/kg. Piga *384*55# kwa taarifa zaidi."
        return self.send_sms(to_phone, msg)
