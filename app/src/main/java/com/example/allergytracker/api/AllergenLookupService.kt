package com.example.allergytracker.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface AllergenLookupService {
    @GET("parser")
    fun searchFood(
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Query("ingr") ingredient: String,
        //brand: String,
        //upc: String
    ): Call<String>

    @GET("nutrients")
    fun getFoodDetails(
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Query("ingredients") foodId: String
    ): Call<String>

    companion object {
        private const val BASE_URL = "https://api.edamam.com/api/food-database/v2/"

        fun create(): AllergenLookupService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
                .create(AllergenLookupService::class.java)
        }
    }
}