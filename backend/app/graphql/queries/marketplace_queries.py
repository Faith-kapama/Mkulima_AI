from typing import List
import strawberry
from app.core.database import SessionLocal
from app.models.marketplace import Marketplace
from app.schemas.marketplace import MarketplaceProductType

@strawberry.type
class MarketplaceQueries:

    @strawberry.field
    def all_marketplace_products(self) -> List[MarketplaceProductType]:
        db = SessionLocal()
        try:
            items = db.query(Marketplace).filter(Marketplace.is_available == True).all()
            if not items:
                return [
                    MarketplaceProductType(id=1, title="Fresh Organic Maize (Mahindi Safi)", category="Cereals", price=1100.0, unit="kg", quantity=500.0, location="Morogoro, Kilosa", seller_name="Juma Rashidi", seller_phone="+255712345678", is_organic=True, image_url=None, created_at="2026-09-24"),
                    MarketplaceProductType(id=2, title="Ripe Greenhouse Tomatoes", category="Horticulture", price=1700.0, unit="kg", quantity=150.0, location="Mbeya, Uyole", seller_name="Rehema Mushi", seller_phone="+255754987654", is_organic=False, image_url=None, created_at="2026-09-24"),
                ]
            return [
                MarketplaceProductType(
                    id=i.id,
                    title=i.title,
                    category=i.category,
                    price=i.price,
                    unit=i.unit,
                    quantity=i.quantity,
                    location=i.location,
                    seller_name=i.seller_name,
                    seller_phone=i.seller_phone,
                    is_organic=i.is_organic,
                    image_url=i.image_url,
                    created_at=i.created_at.strftime("%Y-%m-%d")
                ) for i in items
            ]
        finally:
            db.close()
