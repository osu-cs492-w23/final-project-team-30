package com.example.allergytracker.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NutrientsPostEntry(
    val foodId: String
)
