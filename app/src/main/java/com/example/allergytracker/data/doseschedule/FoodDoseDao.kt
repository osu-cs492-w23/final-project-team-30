package com.example.allergytracker.data.doseschedule

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDoseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDose(foodDose: FoodDose)
    @Delete
    suspend fun deleteDose(foodDose: FoodDose)

    @Query("SELECT * FROM FoodDose")
    fun getFoodDoses() : Flow<List<FoodDose>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoseSchedule(foodDoseSchedule: FoodDoseSchedule)
    @Delete
    suspend fun deleteDoseSchedule(foodDoseSchedule: FoodDoseSchedule)

    @Query("SELECT * FROM FoodDoseSchedule WHERE foodId = :foodId")
    fun getFoodDoseSchedules(foodId: Long) : Flow<List<FoodDoseSchedule>>
}