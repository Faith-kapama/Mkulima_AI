from fastapi import APIRouter, Form, Response
from app.services.ussd_service import USSDService

router = APIRouter()
ussd_service = USSDService()

@router.post("/ussd")
async def handle_ussd(
    sessionId: str = Form(...),
    serviceCode: str = Form(...),
    phoneNumber: str = Form(...),
    text: str = Form("")
):
    response_text = ussd_service.handle_request(
        session_id=sessionId,
        phone_number=phoneNumber,
        text=text
    )
    return Response(content=response_text, media_type="text/plain")
