def success_response(message: str, end: bool = True) -> str:
    prefix = "END " if end else "CON "
    return f"{prefix}{message}"
