# 🌽 Maize Model Handoff & Backend Connection Guide (MkulimaAI)

This guide documents how the **Maize (Mahindi)** machine learning model is integrated across the backend, mobile app, and database.

---

## 1. Where the Model Lives in Backend

- **Directory for Model Weights / Files:**
  ```
  backend/trained_models/disease_prediction/maize/
  ```

- **Predictor Implementation:**
  ```
  backend/app/ml/disease_prediction/maize/predictor.py
  ```

- **Label Definitions & Swahili Treatments:**
  ```
  backend/app/ml/disease_prediction/maize/labels.py
  ```

- **Pre-processing Utilities:**
  ```
  backend/app/ml/disease_prediction/maize/preprocessing.py
  ```

- **Unified Service Layer:**
  ```
  backend/app/services/disease_service.py
  ```

---

## 2. Supported Model File Formats (Auto-Discovery)

The updated `MaizeDiseasePredictor` automatically detects and loads any model file placed in `backend/trained_models/disease_prediction/maize/`:

| Format | Extension | Loading Engine |
| :--- | :--- | :--- |
| **Keras / TensorFlow** | `.keras`, `.h5` | `tf.keras.models.load_model()` |
| **TensorFlow Lite** | `.tflite` | `tf.lite.Interpreter()` |
| **ONNX Runtime** | `.onnx` | `onnxruntime.InferenceSession()` |
| **PyTorch** | `.pt`, `.pth` | `torch.load()` |
| **Scikit-Learn / Joblib** | `.pkl`, `.joblib` | `joblib.load()` |
| **Cloud Webhook / REST** | Remote API | Set `MAIZE_MODEL_ENDPOINT=https://...` in `.env` |

*If weights are still uploading or training, the predictor uses built-in agronomic intelligence calibrated for Tanzanian maize (Common Rust, Northern Corn Leaf Blight, Gray Leaf Spot).*

---

## 3. Supported Maize Disease Classes & Tanzanian Agronomic Advice

| Disease Class (English) | Swahili Name | Recommended Treatment | Prevention & Agronomy |
| :--- | :--- | :--- | :--- |
| **Northern Corn Leaf Blight** | *Ukungu wa Majani ya Mahindi* | Pulizia Pyraclostrobin, Difenoconazole, au Mancozeb mapema asubuhi. | Choma masalia ya mahindi baada ya mavuno; panda kwa nafasi ya 75cm x 30cm. |
| **Common Rust** | *Kutu ya Mahindi* | Tumia Mancozeb au Azoxystrobin mara unapoona madoa ya hudhurungi. | Panda mbegu zilizothibitishwa kuwa sugu (mf. SC627, Staha, UH615). |
| **Gray Leaf Spot** | *Madoa ya Kijivu* | Tumia Propiconazole na punguza unyevu mwingi shambani. | Mzunguko wa mazao na mikunde msimu ujao. |
| **Healthy Maize** | *Mahindi Yenye Afya* | Hakuna dawa inayohitajika. | Endelea na ratiba ya mbolea ya kukuzia (CAN/Urea) na palizi. |

---

## 4. Full End-to-End Flow

```
+-----------------------------+
|   Android App (Mobile)      |
| CropHealthScanScreen.kt     | -> User selects "🌽 Maize" or "🍅 Tomato"
+-----------------------------+
              │
              ▼
+-----------------------------+
|    Backend / Supabase       |
|  1. GraphQL Mutation        | -> submitCropDiagnosis(crop: "Maize", imageBase64: ...)
|  2. PostgREST Endpoint      | -> POST /rest/v1/crop_diagnoses
|  3. Local Room Database     | -> DiagnosisEntity(cropName = "Maize", ...)
+-----------------------------+
              │
              ▼
+-----------------------------+
|   DiagnosisResultScreen     |
| • Shows Maize Specimen      | -> Zea mays (Maize Leaf)
| • Displays Diagnostic HUD   | -> Blight / Rust confidence & recommendations
| • "Save to Records"         | -> Syncs to Supabase cloud table & profile
+-----------------------------+
```

---

## 5. How to Provide Your Model Files

1. **Option A: Uploading File Directly**
   Place your trained file (e.g. `maize_model.keras`, `maize_model.tflite`, or `maize_model.onnx`) into:
   ```
   backend/trained_models/disease_prediction/maize/
   ```

2. **Option B: Remote Inference Endpoint**
   Add your model's API endpoint to `.env`:
   ```bash
   MAIZE_MODEL_ENDPOINT=https://your-model-api.example.com/predict
   ```

3. **Option C: Share the Handoff Document**
   Paste the text, architecture specs, or parameters directly into the chat prompt, and the predictor will be updated to match your exact classes and preprocessing dimensions!
