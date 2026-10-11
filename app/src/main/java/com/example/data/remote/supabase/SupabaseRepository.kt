package com.example.data.remote.supabase

import android.util.Log
import com.example.data.local.MockDataProvider
import com.example.data.model.MarketPrice
import com.example.data.model.MarketplaceProduct
import com.example.data.model.PricePoint
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class SupabaseRepository {

  private val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

  private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
    .connectTimeout(15, TimeUnit.SECONDS)
    .readTimeout(15, TimeUnit.SECONDS)
    .addInterceptor(HttpLoggingInterceptor().apply {
      level = HttpLoggingInterceptor.Level.BASIC
    })
    .build()

  private fun getApi(): SupabaseApi? {
    if (!SupabaseConfig.isConfigured) return null
    return try {
      val baseUrl = if (SupabaseConfig.url.endsWith("/")) SupabaseConfig.url else "${SupabaseConfig.url}/"
      Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(SupabaseApi::class.java)
    } catch (e: Exception) {
      Log.w("SupabaseRepo", "Failed to build Retrofit client: ${e.message}")
      null
    }
  }

  suspend fun fetchMarketPrices(): List<MarketPrice> = withContext(Dispatchers.IO) {
    val api = getApi()
    if (api != null) {
      try {
        val authHeader = "Bearer ${SupabaseConfig.anonKey}"
        val response = api.getMarketPrices(
          apiKey = SupabaseConfig.anonKey,
          authHeader = authHeader
        )
        if (response.isSuccessful) {
          val items = response.body()
          if (!items.isNullOrEmpty()) {
            return@withContext items.map { item ->
              val cropName = item["commodity_name"] as? String ?: "Mahindi"
              val market = item["market_name"] as? String ?: "Kariakoo"
              val region = item["region"] as? String ?: "Dar es Salaam"
              val price = (item["price_per_kg"] as? Number)?.toInt() ?: 750
              val trend = item["trend"] as? String ?: "stable"
              val trendPct = (item["change_percentage"] as? Number)?.toDouble() ?: 0.0
              val trendText = if (trendPct >= 0) "+$trendPct%" else "$trendPct%"

              MarketPrice(
                cropName = cropName,
                region = "$market ($region)",
                pricePerKgTzs = price,
                trend = trendText,
                history = listOf(
                  PricePoint("Jul", (price * 0.92).toInt()),
                  PricePoint("Aug", (price * 0.96).toInt()),
                  PricePoint("Sep", price)
                )
              )
            }
          }
        }
      } catch (e: Exception) {
        Log.w("SupabaseRepo", "Error fetching prices from Supabase: ${e.message}")
      }
    }
    // Fallback to initial benchmarks
    listOf(MockDataProvider.maizeMarketPrice, MockDataProvider.tomatoMarketPrice)
  }

  suspend fun fetchMarketplaceProducts(): List<MarketplaceProduct> = withContext(Dispatchers.IO) {
    val api = getApi()
    if (api != null) {
      try {
        val authHeader = "Bearer ${SupabaseConfig.anonKey}"
        val response = api.getMarketplaceProducts(
          apiKey = SupabaseConfig.anonKey,
          authHeader = authHeader
        )
        if (response.isSuccessful) {
          val items = response.body()
          if (!items.isNullOrEmpty()) {
            return@withContext items.map { p ->
              MarketplaceProduct(
                id = p["id"]?.toString() ?: "p-${System.currentTimeMillis()}",
                title = p["title"] as? String ?: "Pembejeo",
                cropType = p["category"] as? String ?: "Mbegu",
                location = p["region"] as? String ?: "Tanzania",
                quantityKg = (p["quantity_available"] as? Number)?.toInt() ?: 1,
                pricePerKgTzs = (p["price_tzs"] as? Number)?.toInt() ?: 10000,
                sellerName = "Muuzaji Aliyethibitishwa",
                sellerPhone = p["seller_phone"] as? String ?: "+255700000000",
                isVerified = true
              )
            }
          }
        }
      } catch (e: Exception) {
        Log.w("SupabaseRepo", "Error fetching products from Supabase: ${e.message}")
      }
    }
    MockDataProvider.defaultProducts
  }

  suspend fun logPrediction(
    cropName: String,
    farmSizeAcres: Double,
    region: String,
    season: String,
    minBags: Double,
    maxBags: Double,
    phoneNumber: String? = null
  ) = withContext(Dispatchers.IO) {
    val api = getApi() ?: return@withContext false
    try {
      val authHeader = "Bearer ${SupabaseConfig.anonKey}"
      val payload = mapOf(
        "crop_name" to cropName,
        "farm_size_acres" to farmSizeAcres,
        "region" to region,
        "season" to season,
        "estimated_min_bags" to minBags,
        "estimated_max_bags" to maxBags,
        "phone_number" to phoneNumber
      )
      val response = api.logYieldPrediction(
        apiKey = SupabaseConfig.anonKey,
        authHeader = authHeader,
        payload = payload
      )
      response.isSuccessful
    } catch (e: Exception) {
      Log.w("SupabaseRepo", "Failed to log prediction to Supabase: ${e.message}")
      false
    }
  }

  suspend fun saveFarmerProfile(
    fullName: String,
    phoneNumber: String,
    region: String,
    district: String,
    farmSizeAcres: Double,
    primaryCrop: String
  ) = withContext(Dispatchers.IO) {
    val api = getApi() ?: return@withContext false
    try {
      val authHeader = "Bearer ${SupabaseConfig.anonKey}"
      val payload = mapOf(
        "full_name" to fullName,
        "phone_number" to phoneNumber,
        "region" to region,
        "district" to district,
        "farm_size_acres" to farmSizeAcres,
        "primary_crop" to primaryCrop
      )
      val response = api.saveFarmerProfile(
        apiKey = SupabaseConfig.anonKey,
        authHeader = authHeader,
        payload = payload
      )
      response.isSuccessful
    } catch (e: Exception) {
      Log.w("SupabaseRepo", "Failed to save profile to Supabase: ${e.message}")
      false
    }
  }

  suspend fun saveCropDiagnosis(
    cropName: String,
    diseaseName: String,
    diseaseNameSw: String,
    confidence: Int,
    severity: String,
    treatment: String,
    prevention: String,
    phoneNumber: String? = null
  ) = withContext(Dispatchers.IO) {
    val api = getApi() ?: return@withContext false
    try {
      val authHeader = "Bearer ${SupabaseConfig.anonKey}"
      val payload = mapOf(
        "crop_name" to cropName,
        "disease_name" to diseaseName,
        "disease_name_sw" to diseaseNameSw,
        "confidence" to confidence,
        "severity" to severity,
        "treatment" to treatment,
        "prevention" to prevention,
        "phone_number" to phoneNumber
      )
      val response = api.saveCropDiagnosis(
        apiKey = SupabaseConfig.anonKey,
        authHeader = authHeader,
        payload = payload
      )
      response.isSuccessful
    } catch (e: Exception) {
      Log.w("SupabaseRepo", "Failed to save diagnosis to Supabase: ${e.message}")
      false
    }
  }

  suspend fun syncCloudDiagnosesToLocal(dao: com.example.data.local.DiagnosisDao, phoneNumber: String? = null) = withContext(Dispatchers.IO) {
    val api = getApi() ?: return@withContext
    try {
      val authHeader = "Bearer ${SupabaseConfig.anonKey}"
      val response = api.getCropDiagnoses(
        apiKey = SupabaseConfig.anonKey,
        authHeader = authHeader
      )
      if (response.isSuccessful) {
        val remoteList = response.body() ?: emptyList()
        for (item in remoteList) {
          val crop = item["crop_name"] as? String ?: "Maize"
          val disease = item["disease_name"] as? String ?: "Crop Blight"
          val conf = (item["confidence"] as? Number)?.toInt() ?: 90
          val treatment = item["treatment"] as? String ?: ""
          val prevention = item["prevention"] as? String ?: ""
          val recs = if (treatment.isNotBlank()) "$treatment\n$prevention" else "Fanya ukaguzi wa shamba mara kwa mara."

          dao.insertDiagnosis(
            com.example.data.local.DiagnosisEntity(
              cropName = crop,
              diseaseName = disease,
              confidence = conf,
              recommendations = recs
            )
          )
        }
      }
    } catch (e: Exception) {
      Log.w("SupabaseRepo", "Failed syncing cloud diagnoses to local: ${e.message}")
    }
  }

  suspend fun createMarketplaceProduct(
    title: String,
    category: String,
    cropType: String,
    priceTzs: Double,
    unit: String,
    region: String,
    sellerName: String,
    sellerPhone: String,
    isBuyerRequest: Boolean = false
  ) = withContext(Dispatchers.IO) {
    val api = getApi() ?: return@withContext false
    try {
      val authHeader = "Bearer ${SupabaseConfig.anonKey}"
      val payload = mapOf(
        "title" to title,
        "category" to category,
        "crop_type" to cropType,
        "price_tzs" to priceTzs,
        "unit" to unit,
        "region" to region,
        "seller_name" to sellerName,
        "seller_phone" to sellerPhone,
        "is_buyer_request" to isBuyerRequest,
        "status" to "active"
      )
      val response = api.createMarketplaceProduct(
        apiKey = SupabaseConfig.anonKey,
        authHeader = authHeader,
        payload = payload
      )
      response.isSuccessful
    } catch (e: Exception) {
      Log.w("SupabaseRepo", "Failed to create product on Supabase: ${e.message}")
      false
    }
  }

  suspend fun fetchWeatherAdvisory(region: String): Pair<String, String>? = withContext(Dispatchers.IO) {
    val api = getApi() ?: return@withContext null
    try {
      val authHeader = "Bearer ${SupabaseConfig.anonKey}"
      val response = api.getWeatherAdvisories(
        apiKey = SupabaseConfig.anonKey,
        authHeader = authHeader
      )
      if (response.isSuccessful) {
        val list = response.body()
        val found = list?.firstOrNull { (it["region"] as? String)?.equals(region, ignoreCase = true) == true }
          ?: list?.firstOrNull()
        if (found != null) {
          val forecast = found["rainfall_forecast"] as? String ?: "Hali nzuri ya mvua."
          val advisory = found["farming_advisory"] as? String ?: "Endelea na kanuni bora za kilimo."
          return@withContext forecast to advisory
        }
      }
    } catch (e: Exception) {
      Log.w("SupabaseRepo", "Failed to fetch weather from Supabase: ${e.message}")
    }
    null
  }
}
