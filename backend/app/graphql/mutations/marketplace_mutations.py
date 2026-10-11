import strawberry
from typing import Optional
from datetime import datetime
from app.core.database import SessionLocal
from app.models.marketplace import Marketplace
from app.schemas.marketplace import MarketplaceMutationPayload, MarketplaceProductType

@strawberry.type
class MarketplaceMutations:

    @strawberry.mutation
    def create_marketplace_product(
        self,
        title: str,
        category: str,
        price: float,
        unit: str,
        quantity: float,
        location: str,
        seller_phone: str,
        seller_name: str = "Mkulima",
        is_organic: bool = True
    ) -> MarketplaceMutationPayload:
        db = SessionLocal()
        try:
            item = Marketplace(
                title=title,
                category=category,
                price=price,
                unit=unit,
                quantity=quantity,
                location=location,
                seller_phone=seller_phone,
                seller_name=seller_name,
                is_organic=is_organic,
                created_at=datetime.utcnow()
            )
            db.add(item)
            db.commit()
            db.refresh(item)

            return MarketplaceMutationPayload(
                success=True,
                message="Bidhaa imewekwa sokoni kikamilifu",
                product=MarketplaceProductType(
                    id=item.id,
                    title=item.title,
                    category=item.category,
                    price=item.price,
                    unit=item.unit,
                    quantity=item.quantity,
                    location=item.location,
                    seller_name=item.seller_name,
                    seller_phone=item.seller_phone,
                    is_organic=item.is_organic,
                    image_url=item.image_url,
                    created_at=item.created_at.strftime("%Y-%m-%d")
                )
            )
        finally:
            db.close()
