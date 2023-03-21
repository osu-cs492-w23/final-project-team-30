package com.example.allergytracker.data.allergenlookup

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FoodAllergenDetails(
    val healthLabels: List<String>,
    val cautions: List<String>,
    @Json(name="ingredients") val food: List<FoodParsed>,
)

@JsonClass(generateAdapter = true)
data class FoodParsed(
    val parsed: List<FoodDetailResult>
)

@JsonClass(generateAdapter = true)
data class FoodDetailResult(
    @Json(name = "food") val name: String,
    val foodId: String
)