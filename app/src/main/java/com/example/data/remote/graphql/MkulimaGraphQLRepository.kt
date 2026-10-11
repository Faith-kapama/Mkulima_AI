package com.example.data.remote.graphql

import android.util.Log
import com.example.data.local.MockDataProvider
import com.example.data.model.MarketPrice
import com.example.data.model.MarketplaceProduct
import com.example.data.model.PricePoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MkulimaGraphQLRepository(
  private val api: MkulimaGraphQLApi = GraphQLClientProvider.getApi()
) {

  suspend fun fetchMarketPrices(): List<MarketPrice> = withContext(Dispatchers.IO) {
    try {
      val request = GraphQLRequest(query = GraphQLQueries.ALL_MARKET_PRICES)
      val response = api.execute(
        request = request,
        authHeader = GraphQLClientProvider.authToken?.let { "Bearer $it" }
      )
      if (response.isSuccessful) {
        val dataMap = response.body()?.get("data") as? Map<*, *>
        val pricesList = dataMap?.get("allMarketPrices") as? List<Map<String, Any>>
        if (pricesList != null && pricesList.isNotEmpty()) {
          return@withContext pricesList.map { item ->
            MarketPrice(
              cropName = item["cropName"] as? String ?: "Crop",
              region = item["marketLocation"] as? String ?: "Dodoma",
              pricePerKgTzs = (item["pricePerUnit"] as? Number)?.toInt() ?: 1000,
              trend = item["priceTrend"] as? String ?: "+5%",
              history = listOf(
                PricePoint("Aug", ((item["pricePerUnit"] as? Number)?.toInt() ?: 1000) - 100),
                PricePoint("Sep", (item["pricePerUnit"] as? Number)?.toInt() ?: 1000)
              )
            )
          }
        }
      }
    } catch (e: Exception) {
      Log.w("GraphQLRepository", "GraphQL backend fallback: ${e.message}")
    }
    listOf(MockDataProvider.maizeMarketPrice, MockDataProvider.tomatoMarketPrice)
  }

  suspend fun fetchMarketplaceProducts(): List<MarketplaceProduct> = withContext(Dispatchers.IO) {
    try {
      val request = GraphQLRequest(query = GraphQLQueries.ALL_MARKETPLACE_PRODUCTS)
      val response = api.execute(
        request = request,
        authHeader = GraphQLClientProvider.authToken?.let { "Bearer $it" }
      )
      if (response.isSuccessful) {
        val dataMap = response.body()?.get("data") as? Map<*, *>
        val productsList = dataMap?.get("allMarketplaceProducts") as? List<Map<String, Any>>
        if (productsList != null && productsList.isNotEmpty()) {
          return@withContext productsList.map { p ->
            MarketplaceProduct(
              id = p["id"]?.toString() ?: "remote-${System.currentTimeMillis()}",
              title = p["title"] as? String ?: "Produce",
              cropType = p["category"] as? String ?: "Produce",
              location = p["location"] as? String ?: "Tanzania",
              quantityKg = (p["quantity"] as? Number)?.toInt() ?: 100,
              pricePerKgTzs = (p["price"] as? Number)?.toInt() ?: 1000,
              sellerName = p["sellerName"] as? String ?: "Mkulima",
              sellerPhone = p["sellerPhone"] as? String ?: "+255700000000",
              isVerified = true
            )
          }
        }
      }
    } catch (e: Exception) {
      Log.w("GraphQLRepository", "Using local fallback for marketplace: ${e.message}")
    }
    MockDataProvider.defaultProducts
  }
}
