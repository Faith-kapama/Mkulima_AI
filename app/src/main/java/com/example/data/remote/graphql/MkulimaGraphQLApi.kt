package com.example.data.remote.graphql

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface MkulimaGraphQLApi {

  @POST("graphql/")
  suspend fun execute(
    @Body request: GraphQLRequest,
    @Header("Authorization") authHeader: String? = null
  ): Response<Map<String, Any?>>
}
