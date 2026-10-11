package com.example.data.remote.graphql

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object GraphQLClientProvider {

  // Android emulator localhost maps to 10.0.2.2; configurable for real server
  @Volatile
  var baseUrl: String = "http://10.0.2.2:8000/"

  @Volatile
  var authToken: String? = null

  private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
  }

  private val okHttpClient: OkHttpClient by lazy {
    OkHttpClient.Builder()
      .connectTimeout(30, TimeUnit.SECONDS)
      .readTimeout(30, TimeUnit.SECONDS)
      .writeTimeout(30, TimeUnit.SECONDS)
      .addInterceptor(loggingInterceptor)
      .build()
  }

  private val moshi: Moshi by lazy {
    Moshi.Builder()
      .add(KotlinJsonAdapterFactory())
      .build()
  }

  fun getApi(customBaseUrl: String? = null): MkulimaGraphQLApi {
    val url = customBaseUrl ?: baseUrl
    return Retrofit.Builder()
      .baseUrl(url)
      .client(okHttpClient)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .build()
      .create(MkulimaGraphQLApi::class.java)
  }
}
