package com.example.allergytracker.data.settings

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDoseDao {
    @Insert
    suspend fun insertDose(foodDose: FoodDose)
    @Delete
    suspend fun deleteDose(foodDose: FoodDose)

    @Query("SELECT * FROM FoodDose")
    fun getFoodDoses() : Flow<List<FoodDose>>

    @Insert
    suspend fun insertDoseSchedule(foodDoseSchedule: FoodDoseSchedule)
    @Delete
    suspend fun deleteDoseSchedule(foodDoseSchedule: FoodDoseSchedule)

    @Query("SELECT * FROM FoodDoseSchedule WHERE foodId = :foodId")
    fun getFoodDoseSchedules(foodId: Int) : Flow<List<FoodDoseSchedule>>
}