package com.example.allergytracker.api

import com.example.allergytracker.data.AllergenResult
import com.example.allergytracker.data.FoodAllergenDetails
import com.example.allergytracker.data.NutrientsPostBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface AllergenLookupService {
    @GET("parser")
    suspend fun searchFood(
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Query("ingr") ingredient: String,
        //brand: String,
        //upc: String
    ): Response<AllergenResult>

    @Headers("Content-type: application/json")
    @POST("nutrients")
    suspend fun getFoodDetails(
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Body body: NutrientsPostBody
    ): Response<FoodAllergenDetails>

    companion object {
        private const val BASE_URL = "https://api.edamam.com/api/food-database/v2/"

        fun create(): AllergenLookupService {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val httpBuilder = OkHttpClient.Builder()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(httpBuilder.build())
                .build()
                .create(AllergenLookupService::class.java)
        }
    }
}