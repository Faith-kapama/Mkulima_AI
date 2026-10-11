package com.example.data.model

data class CropDiagnosis(
  val id: String,
  val cropName: String,
  val diseaseName: String,
  val confidence: Int,
  val recommendations: List<String>,
  val timestamp: Long = System.currentTimeMillis(),
  val imageUri: String? = null,
  val isLocalAsset: Boolean = true,
  val notes: String = ""
)

data class YieldPrediction(
  val id: String,
  val cropName: String,
  val farmSizeAcres: Double,
  val location: String,
  val season: String,
  val estimatedYieldMin: Double,
  val estimatedYieldMax: Double,
  val unit: String = "tons per acre",
  val keyFactors: List<String>,
  val timestamp: Long = System.currentTimeMillis()
)

data class PricePoint(
  val month: String,
  val priceTzs: Int
)

data class MarketPrice(
  val cropName: String,
  val region: String,
  val pricePerKgTzs: Int,
  val trend: String = "+5%",
  val history: List<PricePoint>
)

data class MarketplaceProduct(
  val id: String,
  val title: String,
  val cropType: String,
  val location: String,
  val quantityKg: Int,
  val pricePerKgTzs: Int,
  val sellerName: String,
  val sellerPhone: String,
  val isBuyerRequest: Boolean = false,
  val isVerified: Boolean = true,
  val imageUri: String? = null,
  val customDrawableRes: Int? = null
)

data class FarmerProfile(
  val name: String = "Juma Rashidi",
  val phone: String = "+255 754 123 456",
  val email: String = "juma.farmer@mkulima.co.tz",
  val region: String = "Dodoma",
  val district: String = "Chamwino",
  val village: String = "Buigiri",
  val farmSizeAcres: Double = 3.5,
  val primaryCrops: List<String> = listOf("Maize", "Tomato"),
  val farmingType: String = "Smallholder Farmer",
  val waterSource: String = "Rainfed",
  val receiveSmsAlerts: Boolean = true,
  val memberSince: String = "January 2024"
)

/**
 * 15-Feature input payload for Tanzanian Maize XGBoost Regression Model
 * (From Handoff Specification)
 */
data class MaizeYieldInput(
  val region: String = "Dodoma",
  val season: String = "Long rains (Masika)",
  val farmAreaHa: Double = 2.0,
  val seedType: String = "Improved",
  val irrigationUsed: Boolean = false,
  val fertilizerUsed: Boolean = true,
  val fertilizerRateKgHa: Double = 100.0,
  val improvedSeedUsed: Boolean = true,
  val traditionalSeedUsed: Boolean = false,
  val recycledSeedUsed: Boolean = false,
  val seedQuantityKgHa: Double = 30.0,
  val plantingMonth: String = "December",
  // Auto-filled (weather/soil data)
  val rainfallGrowingMm: Double = 570.0,
  val temperatureC: Double = 26.2,
  val soilMoistureIndex: Double = 0.48
)

/**
 * Buyer demand request item shown in Market Prices screen (Image 2)
 */
data class BuyerDemandItem(
  val id: String,
  val buyerName: String,
  val location: String,
  val cropType: String, // "Maize" or "Tomato"
  val neededQuantityKg: Int,
  val offerPricePerKgTzs: Int,
  val contactPhone: String
)

