# 🌾 MkulimaAI Backend (GraphQL, Python, ML & USSD)

<p align="center">
  <img src="../assets/mkulima_cover_logo.jpg" alt="MkulimaAI Cover Banner" width="100%" />
</p>

A Python backend for **MkulimaAI**, providing GraphQL APIs for the Android client, USSD gateway support (*384*55#), and Machine Learning modules for crop disease and yield prediction.

---

## 📁 Architecture Overview

```
backend/
├── app/
│   ├── main.py                     # FastAPI / Uvicorn + GraphQL Server
│   ├── core/                       # Database, JWT auth, config
│   ├── models/                     # SQLAlchemy models (User, Farmer, Crop, Disease, MarketPrice, Marketplace, USSD)
│   ├── schemas/                    # Pydantic & Strawberry GraphQL Types
│   ├── graphql/                    # Root GraphQL schema, queries & mutations
│   ├── api/                        # USSD gateway webhook endpoints
│   ├── services/                   # Business logic (Disease, USSD, SMS, Market)
│   ├── ml/                         # ML inference (Maize & Tomato disease models, yield prediction)
│   └── integrations/              # Weather, Africa's Talking SMS/USSD
├── requirements.txt
├── .env.example
└── scripts/
    └── seed_database.py            # Seed initial Tanzanian market & crop data
```

---

## 🚀 Quickstart

### 1. Set Up Virtual Environment
```bash
python3 -m venv venv
source venv/bin/activate    # On Windows: venv\Scripts\activate
pip install -r requirements.txt
```

### 2. Configure Environment
```bash
cp .env.example .env
```

### 3. Seed Database & Run Server
```bash
python scripts/seed_database.py
uvicorn app.main:app --host 0.0.0.0 --port 8000 --reload
```

---

## 🌐 Endpoints

- **GraphQL Playground & Schema**: [http://localhost:8000/graphql](http://localhost:8000/graphql)
- **USSD Webhook**: `POST http://localhost:8000/api/ussd` (Compatible with Africa's Talking)
- **Health Check**: [http://localhost:8000/](http://localhost:8000/)

---

## 📱 Android Connection

The Android mobile app includes a built-in GraphQL client (`MkulimaGraphQLRepository.kt`).
- When running on an Android emulator, `10.0.2.2:8000` automatically maps to your host machine's `localhost:8000`.
- If the backend is not yet started, the Android app gracefully falls back to local cached data with zero crashes.
