package com.example.allergytracker.data.settings

class FoodDoseRepository(
    private val dao: FoodDoseDao
) {
    suspend fun insertFoodDose(foodDose: FoodDose) = dao.insertDose(foodDose)
    suspend fun deleteFoodDose(foodDose: FoodDose) = dao.deleteDose(foodDose)

    fun getAllFoodDoses() = dao.getFoodDoses()

    suspend fun insertFoodDoseSchedule(schedule: FoodDoseSchedule) = dao.insertDoseSchedule(schedule)
    suspend fun deleteFoodDoseSchedule(schedule: FoodDoseSchedule) = dao.deleteDoseSchedule(schedule)

    fun getFoodDoseSchedules(foodId: Int) = dao.getFoodDoseSchedules(foodId)
}