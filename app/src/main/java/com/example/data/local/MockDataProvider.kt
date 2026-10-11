package com.example.data.local

import com.example.data.model.CropDiagnosis
import com.example.data.model.FarmerProfile
import com.example.data.model.MarketPrice
import com.example.data.model.MarketplaceProduct
import com.example.data.model.PricePoint
import com.example.data.model.YieldPrediction
import com.example.data.model.BuyerDemandItem
import com.example.data.model.MaizeYieldInput

object MockDataProvider {

  val defaultDiagnosis = CropDiagnosis(
    id = "diag-001",
    cropName = "Tomato",
    diseaseName = "Tomato Early Blight",
    confidence = 87,
    recommendations = listOf(
      "Remove affected leaves",
      "Use recommended fungicide",
      "Improve plant spacing",
      "Keep the field clean"
    )
  )

  fun predictYield(crop: String, farmSize: Double, location: String, season: String): YieldPrediction {
    val (minRate, maxRate) = when (crop.lowercase()) {
      "maize" -> 2.8 to 3.5
      "tomato" -> 8.5 to 12.0
      "beans" -> 1.2 to 1.8
      else -> 2.0 to 3.0
    }
    return YieldPrediction(
      id = "pred-${System.currentTimeMillis()}",
      cropName = crop,
      farmSizeAcres = farmSize,
      location = location,
      season = season,
      estimatedYieldMin = minRate,
      estimatedYieldMax = maxRate,
      unit = "tons per acre",
      keyFactors = listOf(
        "Good rainfall expected",
        "Suitable for your location",
        "Average soil conditions"
      )
    )
  }

  val maizeMarketPrice = MarketPrice(
    cropName = "Maize",
    region = "Dodoma",
    pricePerKgTzs = 850,
    trend = "+8.5%",
    history = listOf(
      PricePoint("Apr", 700),
      PricePoint("May", 810),
      PricePoint("Jun", 800),
      PricePoint("Jul", 890),
      PricePoint("Aug", 980),
      PricePoint("Sep", 1050)
    )
  )

  val tomatoMarketPrice = MarketPrice(
    cropName = "Tomato",
    region = "Dodoma",
    pricePerKgTzs = 1200,
    trend = "+12.0%",
    history = listOf(
      PricePoint("Apr", 900),
      PricePoint("May", 950),
      PricePoint("Jun", 1100),
      PricePoint("Jul", 1050),
      PricePoint("Aug", 1250),
      PricePoint("Sep", 1380)
    )
  )

  val regionalPricesMaize = listOf(
    "Dodoma" to "850 TZS",
    "Dar es Salaam" to "920 TZS",
    "Arusha" to "880 TZS",
    "Mwanza" to "870 TZS"
  )

  val regionalPricesTomato = listOf(
    "Dodoma" to "1,200 TZS",
    "Dar es Salaam" to "1,450 TZS",
    "Arusha" to "1,150 TZS",
    "Mwanza" to "1,300 TZS"
  )

  val defaultProducts = listOf(
    MarketplaceProduct(
      id = "prod-1",
      title = "Fresh Tomatoes",
      cropType = "Tomato",
      location = "Dodoma",
      quantityKg = 1000,
      pricePerKgTzs = 800,
      sellerName = "Bakari Mwamba",
      sellerPhone = "+255 784 112 233"
    ),
    MarketplaceProduct(
      id = "prod-2",
      title = "Maize (Dry)",
      cropType = "Maize",
      location = "Arusha",
      quantityKg = 2000,
      pricePerKgTzs = 700,
      sellerName = "Amina Nassoro",
      sellerPhone = "+255 754 998 877"
    ),
    MarketplaceProduct(
      id = "prod-3",
      title = "Tomatoes",
      cropType = "Tomato",
      location = "Mwanza",
      quantityKg = 500,
      pricePerKgTzs = 850,
      sellerName = "Joseph Kweka",
      sellerPhone = "+255 712 334 455"
    ),
    MarketplaceProduct(
      id = "prod-4",
      title = "Yellow Maize",
      cropType = "Maize",
      location = "Morogoro",
      quantityKg = 1500,
      pricePerKgTzs = 720,
      sellerName = "Daudi Msigwa",
      sellerPhone = "+255 768 554 321"
    )
  )

  val buyerRequests = listOf(
    MarketplaceProduct(
      id = "req-1",
      title = "Need 5,000 kg Grade A Maize",
      cropType = "Maize",
      location = "Dar es Salaam",
      quantityKg = 5000,
      pricePerKgTzs = 900,
      sellerName = "Kariakoo Wholesale Mills",
      sellerPhone = "+255 755 000 111",
      isBuyerRequest = true
    ),
    MarketplaceProduct(
      id = "req-2",
      title = "Looking for Ripe Red Tomatoes",
      cropType = "Tomato",
      location = "Dodoma City",
      quantityKg = 800,
      pricePerKgTzs = 850,
      sellerName = "Dodoma Fresh Grocers",
      sellerPhone = "+255 714 222 333",
      isBuyerRequest = true
    )
  )

  val defaultProfile = FarmerProfile()

  val buyersSeekingMaize = listOf(
    BuyerDemandItem(
      id = "bm-1",
      buyerName = "Kariakoo Wholesale Mills",
      location = "Dar es Salaam",
      cropType = "Maize",
      neededQuantityKg = 5000,
      offerPricePerKgTzs = 880,
      contactPhone = "+255 754 888 123"
    ),
    BuyerDemandItem(
      id = "bm-2",
      buyerName = "Mbeya Grain Processors",
      location = "Mbeya Town",
      cropType = "Maize",
      neededQuantityKg = 10000,
      offerPricePerKgTzs = 850,
      contactPhone = "+255 784 555 901"
    ),
    BuyerDemandItem(
      id = "bm-3",
      buyerName = "Arusha Animal Feeds Ltd",
      location = "Unga Ltd, Arusha",
      cropType = "Maize",
      neededQuantityKg = 3500,
      offerPricePerKgTzs = 890,
      contactPhone = "+255 713 444 876"
    )
  )

  val buyersSeekingTomatoes = listOf(
    BuyerDemandItem(
      id = "bt-1",
      buyerName = "Dodoma Fresh Grocers",
      location = "Majengo, Dodoma",
      cropType = "Tomato",
      neededQuantityKg = 800,
      offerPricePerKgTzs = 1250,
      contactPhone = "+255 714 222 333"
    ),
    BuyerDemandItem(
      id = "bt-2",
      buyerName = "Tandale Market Traders",
      location = "Tandale, Dar es Salaam",
      cropType = "Tomato",
      neededQuantityKg = 1500,
      offerPricePerKgTzs = 1400,
      contactPhone = "+255 768 777 444"
    ),
    BuyerDemandItem(
      id = "bt-3",
      buyerName = "Kilimanjaro Hotels Catering",
      location = "Moshi, Kilimanjaro",
      cropType = "Tomato",
      neededQuantityKg = 600,
      offerPricePerKgTzs = 1350,
      contactPhone = "+255 755 333 999"
    )
  )

  fun calculateXGBoostMaizeYield(input: MaizeYieldInput): Double {
    val baseTha = 1.65
    val seedFactor = when (input.seedType.lowercase()) {
      "hybrid" -> 0.45
      "improved" -> 0.35
      "local" -> -0.15
      else -> 0.0
    }
    val fertFactor = if (input.fertilizerUsed && input.fertilizerRateKgHa > 0) {
      minOf(0.65, 0.22 * kotlin.math.ln(1.0 + input.fertilizerRateKgHa / 25.0))
    } else {
      -0.20
    }
    val irrigFactor = if (input.irrigationUsed) 0.30 else 0.0
    val rainDiff = (input.rainfallGrowingMm - 750.0) / 750.0
    val rainFactor = maxOf(-0.35, minOf(0.30, rainDiff * 0.4))
    val tempDiff = kotlin.math.abs(input.temperatureC - 24.0)
    val tempFactor = -0.05 * minOf(tempDiff, 4.0)
    val soilFactor = (input.soilMoistureIndex - 0.50) * 0.45
    val seasonFactor = if (input.season.contains("Masika", ignoreCase = true)) 0.15 else -0.10
    val rateDiff = kotlin.math.abs(input.seedQuantityKgHa - 30.0)
    val seedRateFactor = -0.015 * minOf(rateDiff, 15.0)

    val rawYield = baseTha + seedFactor + fertFactor + irrigFactor + rainFactor + tempFactor + soilFactor + seasonFactor + seedRateFactor
    return maxOf(0.8, minOf(4.8, kotlin.math.round(rawYield * 10) / 10.0))
  }
}
