import json
from app.core.database import SessionLocal
from app.models.ussd_session import USSDSession

class SessionManager:

    @staticmethod
    def get_or_create(session_id: str, phone_number: str, service_code: str):
        db = SessionLocal()
        try:
            session = db.query(USSDSession).filter(USSDSession.session_id == session_id).first()
            if not session:
                session = USSDSession(
                    session_id=session_id,
                    phone_number=phone_number,
                    service_code=service_code,
                    current_step="MAIN_MENU",
                    session_data="{}"
                )
                db.add(session)
                db.commit()
                db.refresh(session)
            return session
        finally:
            db.close()
