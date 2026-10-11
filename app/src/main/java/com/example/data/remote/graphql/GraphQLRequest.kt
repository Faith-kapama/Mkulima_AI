package com.example.data.remote.graphql

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GraphQLRequest(
  @Json(name = "query") val query: String,
  @Json(name = "variables") val variables: Map<String, Any?>? = null,
  @Json(name = "operationName") val operationName: String? = null
)

@JsonClass(generateAdapter = true)
data class GraphQLResponse<T>(
  @Json(name = "data") val data: T? = null,
  @Json(name = "errors") val errors: List<GraphQLError>? = null
)

@JsonClass(generateAdapter = true)
data class GraphQLError(
  @Json(name = "message") val message: String,
  @Json(name = "locations") val locations: List<ErrorLocation>? = null
)

@JsonClass(generateAdapter = true)
data class ErrorLocation(
  @Json(name = "line") val line: Int,
  @Json(name = "column") val column: Int
)
