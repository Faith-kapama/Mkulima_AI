import strawberry
from typing import Optional

@strawberry.type
class MarketplaceProductType:
    id: int
    title: str
    category: str
    price: float
    unit: str
    quantity: float
    location: str
    seller_name: str
    seller_phone: str
    is_organic: bool
    image_url: Optional[str] = None
    created_at: str

@strawberry.type
class MarketplaceMutationPayload:
    success: bool
    message: str
    product: Optional[MarketplaceProductType] = None
