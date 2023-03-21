package com.example.allergytracker.data.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.allergytracker.data.AppDatabase
import kotlinx.coroutines.launch

class FoodDoseViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FoodDoseRepository(AppDatabase.getInstance(application).foodDoseDao())

    fun addFoodDose(foodDose: FoodDose) {
        viewModelScope.launch {
            repository.insertFoodDose(foodDose)
        }
    }
    fun remFoodDose(foodDose: FoodDose) {
        viewModelScope.launch {
            repository.deleteFoodDose(foodDose)
        }
    }

    val foodDoses = repository.getAllFoodDoses().asLiveData()

    fun addFoodDoseSchedule(foodDoseSchedule: FoodDoseSchedule) {
        viewModelScope.launch {
            repository.insertFoodDoseSchedule(foodDoseSchedule)
        }
    }
    fun remFoodDoseSchedule(foodDoseSchedule: FoodDoseSchedule) {
        viewModelScope.launch {
            repository.deleteFoodDoseSchedule(foodDoseSchedule)
        }
    }

    fun loadFoodDoseSchedules(foodId: Int) = repository.getFoodDoseSchedules(foodId).asLiveData()
}