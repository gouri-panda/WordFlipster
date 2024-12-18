package com.javix.wordflipster

import com.google.gson.Gson

data class Level(
    val level: Int,
    val details: Details
)

data class Details(
    val quote: String,
    val title: String,
    val born: String,
    val death: String
)

data class JsonResponse(
    val levels: List<Level>
)
fun parseJson(json: String): JsonResponse {
    val gson = Gson()
    return gson.fromJson(json, JsonResponse::class.java)
}