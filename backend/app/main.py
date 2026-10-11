import os
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from strawberry.fastapi import GraphQLRouter
from app.core.config import settings
from app.core.database import Base, engine
from app.graphql.schema import schema
from app.api.ussd import router as ussd_router
from app.api.crop import router as crop_router

# Initialize tables
Base.metadata.create_all(bind=engine)

app = FastAPI(
    title=settings.PROJECT_NAME,
    description="Backend API with GraphQL, USSD, and ML for MkulimaAI Tanzania",
    version="1.0.0"
)

# Enable CORS for Android client and web dashboards
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

graphql_app = GraphQLRouter(schema)

# GraphQL endpoint
app.include_router(graphql_app, prefix="/graphql")

# USSD Africa's Talking Webhook
app.include_router(ussd_router, prefix="/api")

# Crop Intelligence & Diagnosis REST API
app.include_router(crop_router, prefix="/api")

@app.get("/")
def health_check():
    return {
        "status": "online",
        "app": settings.PROJECT_NAME,
        "graphql_endpoint": "/graphql",
        "ussd_webhook": "/api/ussd"
    }

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("app.main:app", host=settings.HOST, port=settings.PORT, reload=True)
