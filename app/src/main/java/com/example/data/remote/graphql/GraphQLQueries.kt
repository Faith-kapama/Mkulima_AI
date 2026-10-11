package com.example.data.remote.graphql

object GraphQLQueries {

  val ALL_MARKET_PRICES = """
    query GetMarketPrices {
      allMarketPrices {
        id
        cropName
        category
        marketLocation
        pricePerUnit
        unit
        priceTrend
        lastUpdated
      }
    }
  """.trimIndent()

  val ALL_MARKETPLACE_PRODUCTS = """
    query GetMarketplaceProducts {
      allMarketplaceProducts {
        id
        title
        category
        price
        unit
        quantity
        location
        sellerName
        sellerPhone
        isOrganic
        imageUrl
        createdAt
      }
    }
  """.trimIndent()

  val GET_FARMER_PROFILE = """
    query GetFarmerProfile {
      farmerProfile {
        id
        fullName
        phoneNumber
        location
        farmSizeAcres
        primaryCrops
        experienceYears
        memberTier
      }
    }
  """.trimIndent()

  val SUBMIT_DIAGNOSIS_MUTATION = """
    mutation SubmitCropDiagnosis(${'$'}crop: String!, ${'$'}imageBase64: String!, ${'$'}symptoms: String) {
      submitCropDiagnosis(crop: ${'$'}crop, imageBase64: ${'$'}imageBase64, symptoms: ${'$'}symptoms) {
        success
        message
        diagnosis {
          id
          crop
          detectedDisease
          confidenceScore
          severity
          treatmentRecommendations
          preventionTips
          estimatedYieldImpactPercent
        }
      }
    }
  """.trimIndent()

  val PREDICT_YIELD_MUTATION = """
    mutation PredictYield(${'$'}crop: String!, ${'$'}acres: Float!, ${'$'}soilType: String!, ${'$'}rainfall: Float, ${'$'}fertilizerUsed: Boolean!) {
      predictCropYield(crop: ${'$'}crop, acres: ${'$'}acres, soilType: ${'$'}soilType, rainfall: ${'$'}rainfall, fertilizerUsed: ${'$'}fertilizerUsed) {
        success
        predictedBags
        predictedTons
        confidencePercent
        optimalHarvestMonth
        recommendations
      }
    }
  """.trimIndent()

  val CREATE_MARKETPLACE_LISTING = """
    mutation CreateMarketplaceProduct(
      ${'$'}title: String!,
      ${'$'}category: String!,
      ${'$'}price: Float!,
      ${'$'}unit: String!,
      ${'$'}quantity: Float!,
      ${'$'}location: String!,
      ${'$'}sellerPhone: String!,
      ${'$'}isOrganic: Boolean!
    ) {
      createMarketplaceProduct(
        title: ${'$'}title,
        category: ${'$'}category,
        price: ${'$'}price,
        unit: ${'$'}unit,
        quantity: ${'$'}quantity,
        location: ${'$'}location,
        sellerPhone: ${'$'}sellerPhone,
        isOrganic: ${'$'}isOrganic
      ) {
        success
        product {
          id
          title
          price
          location
        }
      }
    }
  """.trimIndent()
}
