from app.services.ussd_service import USSDService

def test_ussd_main_menu():
    service = USSDService()
    response = service.handle_request(session_id="sess_001", phone_number="+255712345678", text="")
    assert response.startswith("CON Karibu MkulimaAI")
    assert "1. Bei za Masoko Leo" in response

def test_ussd_market_query():
    service = USSDService()
    response = service.handle_request(session_id="sess_001", phone_number="+255712345678", text="1*1")
    assert response.startswith("END Mahindi (Kariakoo)")
