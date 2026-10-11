from app.ussd.menu import USSDMenu
from app.ussd.responses import success_response

def handle_ussd_step(step: str, user_input: str) -> str:
    if step == "MAIN_MENU":
        return USSDMenu.MAIN_MENU
    return success_response("Asante kwa kutumia MkulimaAI.")
