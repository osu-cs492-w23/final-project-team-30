package com.example.allergytracker.data.allergenlookup

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FoodResult(
    val foodId: String,
    val label: String,
    @Json(name = "knownAs") val name: String,
    @Json(name = "image") val imageUrl: String?
)