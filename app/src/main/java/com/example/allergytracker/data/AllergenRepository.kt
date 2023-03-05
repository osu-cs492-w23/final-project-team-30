package com.example.allergytracker.data

import android.util.Log
import com.example.allergytracker.api.AllergenLookupService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AllergenRepository(
    private val service: AllergenLookupService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun loadAllergenSearch(appId: String, appKey: String, ingredient: String)
        : Result<List<FoodResult>> = withContext(ioDispatcher) {
        try {
            val response = service.searchFood(appId, appKey, ingredient)

            if (response.isSuccessful) {
                val results: MutableList<FoodResult> = mutableListOf()

                if (response.body() != null) {
                    if (response.body()?.topResult?.isNotEmpty() == true)
                        results.add(response.body()!!.topResult[0].data)
                    if (response.body()?.results?.isNotEmpty() == true) {
                        for (entry in response.body()!!.results!!) {
                            results.add(entry.data)
                        }
                    }
                }

                Result.success(results)
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: java.lang.Exception) {
            Result.failure(e)
        }
    }

    suspend fun loadFoodDetails(appId: String, appKey: String, foodResult: FoodResult)
        : Result<FoodAllergenDetails?> = withContext(ioDispatcher) {
        try {
            val body = NutrientsPostBody(listOf(NutrientsPostEntry(foodResult.foodId)))
            val response = service.getFoodDetails(appId, appKey, body)

            if (response.isSuccessful) {
                Result.success(response.body())
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: java.lang.Exception) {
            Result.failure(e)
        }
    }

    private fun formatFoodId(foodId: String) : String {
        return "{\"ingredients\":[{foodId:\"$foodId\"}]}"
    }
}