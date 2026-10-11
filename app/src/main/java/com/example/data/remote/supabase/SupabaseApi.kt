package com.example.data.remote.supabase

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface SupabaseApi {

  @GET("rest/v1/market_prices")
  suspend fun getMarketPrices(
    @Header("apikey") apiKey: String,
    @Header("Authorization") authHeader: String,
    @Query("select") select: String = "*"
  ): Response<List<Map<String, Any?>>>

  @GET("rest/v1/marketplace_products")
  suspend fun getMarketplaceProducts(
    @Header("apikey") apiKey: String,
    @Header("Authorization") authHeader: String,
    @Query("select") select: String = "*",
    @Query("status") status: String = "eq.active"
  ): Response<List<Map<String, Any?>>>

  @Headers("Prefer: return=representation")
  @POST("rest/v1/yield_predictions")
  suspend fun logYieldPrediction(
    @Header("apikey") apiKey: String,
    @Header("Authorization") authHeader: String,
    @Body payload: Map<String, Any?>
  ): Response<List<Map<String, Any?>>>

  @Headers("Prefer: return=representation")
  @POST("rest/v1/profiles")
  suspend fun saveFarmerProfile(
    @Header("apikey") apiKey: String,
    @Header("Authorization") authHeader: String,
    @Body payload: Map<String, Any?>
  ): Response<List<Map<String, Any?>>>

  @Headers("Prefer: return=representation")
  @POST("rest/v1/crop_diagnoses")
  suspend fun saveCropDiagnosis(
    @Header("apikey") apiKey: String,
    @Header("Authorization") authHeader: String,
    @Body payload: Map<String, Any?>
  ): Response<List<Map<String, Any?>>>

  @GET("rest/v1/crop_diagnoses")
  suspend fun getCropDiagnoses(
    @Header("apikey") apiKey: String,
    @Header("Authorization") authHeader: String,
    @Query("select") select: String = "*",
    @Query("order") order: String = "created_at.desc"
  ): Response<List<Map<String, Any?>>>

  @Headers("Prefer: return=representation")
  @POST("rest/v1/marketplace_products")
  suspend fun createMarketplaceProduct(
    @Header("apikey") apiKey: String,
    @Header("Authorization") authHeader: String,
    @Body payload: Map<String, Any?>
  ): Response<List<Map<String, Any?>>>

  @GET("rest/v1/weather_advisories")
  suspend fun getWeatherAdvisories(
    @Header("apikey") apiKey: String,
    @Header("Authorization") authHeader: String,
    @Query("select") select: String = "*"
  ): Response<List<Map<String, Any?>>>
}
