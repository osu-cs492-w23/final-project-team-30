package com.example.allergytracker.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FoodEntry(
    @Json(name = "food") val data: FoodResult
)
