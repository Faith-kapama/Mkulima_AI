# 🌾 MkulimaAI — Smart Farming Assistant for Tanzania

<p align="center">
  <img src="assets/mkulima_cover_logo.jpg" alt="MkulimaAI Cover Banner" width="100%" style="border-radius: 12px; box-shadow: 0 4px 20px rgba(0,0,0,0.15);" />
</p>

<p align="center">
  <b>Akili Bandia kwa Kilimo Bora na Chenye Tija Tanzania</b><br>
  <i>An AI-Powered Agricultural Ecosystem empowering smallholder farmers across all 31 regions of Tanzania with real-time leaf disease diagnosis, agro-ecological yield forecasting, live commodity market prices, and digital agricultural inputs.</i>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android%20%7C%20Jetpack%20Compose-2e7d32?style=for-the-badge&logo=android" alt="Platform" />
  <img src="https://img.shields.io/badge/Backend-Supabase%20%7C%20PostgreSQL%2015-3ecf8e?style=for-the-badge&logo=supabase" alt="Backend" />
  <img src="https://img.shields.io/badge/Language-Kotlin%20%26%20Python-388e3c?style=for-the-badge&logo=kotlin" alt="Language" />
  <img src="https://img.shields.io/badge/Coverage-31%20Tanzanian%20Regions-fbc02d?style=for-the-badge" alt="Coverage" />
  <img src="https://img.shields.io/badge/Status-Production%20Ready-success?style=for-the-badge" alt="Status" />
</p>

---

## 🌟 Overview

**MkulimaAI** is an end-to-end smart agricultural mobile platform tailored specifically for East African agronomy. Developed to tackle food security challenges, crop loss, and price volatility, MkulimaAI bridges advanced Machine Learning directly to farmers' fields via Android smartphones, feature phones (USSD/SMS), and robust cloud synchronisation.

Whether online in urban markets or deep in rural farms with spotty network coverage, MkulimaAI provides instant diagnostics and agronomic advisories in both **Swahili** and **English**.

---

## 🚀 Key Features

### 1. 🔍 AI Crop Health Scan (Maize & Tomato Diagnostics)
* **Computer Vision Diagnostics**: Instant detection of major diseases affecting **Maize (*Mahindi*)** and **Tomato (*Nyanya*)** foliage:
  * *Maize*: Common Rust (*Kutu ya Mahindi*), Northern Corn Leaf Blight (*Ukungu wa Majani*), Grey Leaf Spot (*Madoa Kijivu*), Maize Streak Virus (*Virusi vya Mistari*).
  * *Tomato*: Early Blight (*Ugonjwa wa Ukungu wa Mapema*), Late Blight (*Ukungu Mkubwa*), Bacterial Spot (*Madoa ya Bakteria*), Tomato Yellow Leaf Curl (*Majani Kujikunja*).
* **Specimen Foliage Verification**: Intelligent verification layer filters out non-crop images (shoes, soil, vehicles, foreign objects) and instructs farmers on optimal leaf photography distance (15–30 cm).
* **Localized Treatment Recommendations**: Actionable organic and chemical treatment advice (e.g., Mancozeb, Ridomil Gold, Pyraclostrobin, crop rotation, soil sanitation).

### 2. 📊 Yield Prediction Engine
* **Agro-Ecological Forecasting**: Predicts expected harvest yield (bags / *magunia*) based on farm size (acres), soil moisture, localized rainfall patterns, and agricultural season.
* **Tanzania's 3 Strict Agricultural Seasons**:
  1. **Vuli** (Short Rains / *Mvua Fupi*: Oct – Dec)
  2. **Masika** (Long Rains / *Mvua Ndefu*: Mar – May)
  3. **Kipupwe** (Cool Dry Season / *Msimu wa Baridi*: Jun – Aug)
* **Comprehensive Regional Calibration**: Pre-calibrated across all **31 administrative regions** of Tanzania (from Arusha to Zanzibar).

### 3. 📈 Live Tanzanian Market Commodity Prices
* Real-time market monitoring from major agricultural trading hubs:
  * *Kariakoo (Dar es Salaam)*, *Tandale*, *Majengo (Dodoma)*, *Mwanjelwa (Mbeya)*, *Central Market (Arusha)*, and more.
* Historical price tracking, percentage fluctuations, and seasonal harvest market trends.

### 4. 🛒 Mkulima Agricultural Marketplace
* Browse and purchase certified seeds (e.g., *DKC 8031, SC 719, Eden F1*), organic fertilizers, and registered crop protection products.
* Connect directly with verified agro-dealers across regions via phone and WhatsApp.

### 5. 🛡️ Dual-Layer History & Account Protection (Never Lost)
* **Local Offline Storage (Room SQLite)**: All scans, diagnoses, and custom inputs are saved immediately on the device via Android Room.
* **Cloud Synchronisation (Supabase PostgreSQL)**: Every scan, profile update, and prediction automatically syncs to your cloud database.
* **Persistent Sessions**: Farmers stay signed in across app launches with their profile, phone number, and location saved safely.

### 6. 🌐 Multi-Channel Support (USSD & SMS Gateway)
* Integrates with Africa's Talking gateway for feature phone access via shortcode `*384*55#`, enabling offline farmers to check market prices and submit SMS health alerts.

---

## 🏗️ Architecture & Technology Stack

```
+-----------------------------------------------------------------------------------+
|                        MkulimaAI Android Mobile App                               |
|        Jetpack Compose • Material Design 3 • Kotlin Coroutines & Flow             |
|              Room SQLite (Offline First) • Retrofit & Moshi REST                  |
+-----------------------------------------------------------------------------------+
                                          │
                     HTTPS REST (PostgREST) / Cloud Sync
                                          ▼
+-----------------------------------------------------------------------------------+
|                           SUPABASE CLOUD BACKEND                                  |
|                                                                                   |
|  • PostgreSQL 15+ Relational Database       • Row Level Security (RLS)            |
|  • GoTrue Authentication (Phone / Email)    • Storage Buckets (Crop Scans)        |
|  • Realtime Broadcast & Triggers            • RESTful PostgREST API               |
+-----------------------------------------------------------------------------------+
                                          │
                                          ▼
+-----------------------------------------------------------------------------------+
|                         STANDALONE PYTHON ML ENGINE                               |
|                     FastAPI • GraphQL • TensorFlow / PyTorch                      |
|       Maize & Tomato VisionNet Models • USSD Africa's Talking Webhooks            |
+-----------------------------------------------------------------------------------+
```

| Layer | Technology |
|---|---|
| **Mobile OS** | Android 8.0+ (API 26 to API 35) |
| **Mobile Framework** | Kotlin, Jetpack Compose, Material Design 3 |
| **Local Database** | Android Jetpack Room (SQLite) |
| **Cloud Database** | Supabase PostgreSQL 15 with Row-Level Security |
| **API Layer** | Retrofit 2, Moshi, OkHttp 4 |
| **Backend API (Optional)** | FastAPI, Python 3.11, Strawberry GraphQL, Uvicorn |
| **AI Models** | Custom VisionNet Classifiers & Yield Predictors (`.json`, `.tflite`, `.keras`, `.onnx`) |

---

## 📁 Project Structure

```
MkulimaAI/
├── app/                                 # Android Mobile Application Source
│   ├── src/main/java/com/example/
│   │   ├── data/
│   │   │   ├── local/                   # Room DB (AppDatabase, DiagnosisDao, Entity)
│   │   │   │   └── FarmerProfileManager.kt  # Profile & Session persistence
│   │   │   ├── remote/
│   │   │   │   ├── supabase/            # Supabase API, Config & Repository
│   │   │   │   └── graphql/             # GraphQL API & Repository
│   │   │   └── model/
│   │   │       ├── Models.kt            # Core Domain Models
│   │   │       └── TanzaniaLocations.kt # All 31 Regions & 180+ Districts
│   │   ├── ui/
│   │   │   ├── screens/                 # Compose UI Screens (Scan, Yield, Market, Profile)
│   │   │   ├── components/              # Shared UI Widgets, Cards, Headers
│   │   │   ├── navigation/              # Type-Safe App Navigation
│   │   │   └── theme/                   # Material 3 Color Schemes & Typography
│   │   └── util/
│   │       ├── LanguageManager.kt       # Swahili & English Localization
│   │       └── SpecimenVerificationUtil.kt # Foliage validation logic
│   └── src/main/res/                    # Drawables, Adaptive Icons, Strings
├── backend/                             # Python Backend & ML Microservice
│   ├── app/
│   │   ├── main.py                      # FastAPI + GraphQL + Webhooks entry point
│   │   ├── ml/                          # Maize & Tomato disease & yield predictors
│   │   ├── ussd/                        # Africa's Talking USSD menu handlers
│   │   └── models/                      # SQLAlchemy Database Models
│   ├── trained_models/                  # Active Machine Learning Model weights
│   │   ├── disease_prediction/maize/    # Maize disease model files
│   │   ├── disease_prediction/tomato/   # Tomato disease model files
│   │   └── yield_prediction/            # Yield prediction model files
│   ├── requirements.txt                 # Python dependencies
│   └── scripts/seed_database.py         # Database seeder script
├── assets/
│   └── mkulima_cover_logo.jpg           # Official MkulimaAI Cover Banner
├── supabase_full_backend.sql            # Master Supabase PostgreSQL SQL setup script
└── MkulimaAI.apk                        # Compiled Android APK (Ready for device install)
```

---

## ⚡ Quick Start & Installation

### Option 1: Install the Android App Directly
A compiled Android APK is pre-built in the repository root:
1. Transfer `MkulimaAI.apk` to your Android device (or download directly on mobile).
2. Tap the APK to install (enable *"Install unknown apps"* if prompted).
3. Launch **MkulimaAI** — all local features, offline disease models, and regional calculators work immediately!

---

### Option 2: Run in Android Studio
1. Open the repository root in **Android Studio Ladybug (or newer)**.
2. Allow Gradle to synchronize dependencies.
3. Select an Android device or emulator (API 26+) and click **Run (Shift + F10)**.

---

### Option 3: Connect Your Supabase Cloud Database
1. Go to your [Supabase Dashboard](https://supabase.com/dashboard).
2. Open the **SQL Editor** tab.
3. Open `supabase_full_backend.sql` from this repository, copy its content, paste it into the SQL Editor, and click **Run**.
4. All tables (`profiles`, `crop_diagnoses`, `market_prices`, `yield_predictions`, `marketplace_products`) will be created with sample Tanzanian market data.
5. In `app/src/main/java/com/example/data/remote/supabase/SupabaseConfig.kt`, verify your Project URL and Anon Public Key:
   ```kotlin
   object SupabaseConfig {
       var url: String = "https://your-project-id.supabase.co"
       var anonKey: String = "your-anon-public-key"
   }
   ```

---

### Option 4: Run the Standalone Python ML & USSD Server (Optional)
If you wish to host the local FastAPI and USSD webhook server:

```bash
# 1. Navigate to backend directory
cd backend

# 2. Create and activate a virtual environment
python3 -m venv venv
source venv/bin/activate       # On Windows: venv\Scripts\activate

# 3. Install required packages
pip install -r requirements.txt

# 4. Run the server
uvicorn app.main:app --host 0.0.0.0 --port 8000 --reload
```

- **Interactive API Documentation (Swagger)**: `http://localhost:8000/docs`
- **Interactive GraphQL Explorer**: `http://localhost:8000/graphql`
- **USSD Webhook Endpoint**: `http://localhost:8000/api/ussd`

---

## 📍 Tanzanian Regional & District Coverage

MkulimaAI includes native support for all **31 administrative regions** of the United Republic of Tanzania and their corresponding districts:

| Zone | Regions Covered |
|---|---|
| **Northern Zone** | Arusha, Kilimanjaro, Manyara, Tanga |
| **Central Zone** | Dodoma, Singida |
| **Lake Zone** | Mwanza, Geita, Kagera, Mara, Simiyu, Shinyanga |
| **Southern Highlands** | Mbeya, Iringa, Njombe, Ruvuma, Songwe, Rukwa, Katavi |
| **Coastal Zone** | Dar es Salaam, Pwani, Morogoro |
| **Southern Coast** | Lindi, Mtwara |
| **Western Zone** | Tabora, Kigoma |
| **Zanzibar** | Mjini Magharibi, Kaskazini Unguja, Kusini Unguja, Kaskazini Pemba, Kusini Pemba |

---

## 🌿 Supported Crops & Pathogens

* **🌽 Maize (Zea mays / *Mahindi*)**:
  - *Healthy Foliage*
  - *Common Rust (Puccinia sorghi)*
  - *Northern Corn Leaf Blight (Exserohilum turcicum)*
  - *Grey Leaf Spot (Cercospora zeae-maydis)*
  - *Maize Streak Disease (MSV)*
* **🍅 Tomato (Solanum lycopersicum / *Nyanya*)**:
  - *Healthy Foliage*
  - *Early Blight (Alternaria solani)*
  - *Late Blight (Phytophthora infestans)*
  - *Bacterial Spot (Xanthomonas spp.)*
  - *Tomato Yellow Leaf Curl Virus (TYLCV)*

---

## 📄 License & Ownership

Developed with pride for Tanzanian and East African agricultural communities.  
Distributed under the **MIT License**. Free for commercial and non-commercial community agricultural initiatives.

*MkulimaAI — Kulima Kijanja, Mavuno Mengi!*
