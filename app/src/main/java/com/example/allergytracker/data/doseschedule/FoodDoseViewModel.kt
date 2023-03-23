package com.example.allergytracker.data.doseschedule

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.allergytracker.data.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FoodDoseViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FoodDoseRepository(AppDatabase.getInstance(application).foodDoseDao())

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _foodDoseSchedules = MutableLiveData<List<FoodDoseSchedule>?>(null)
    val foodDoseSchedules: LiveData<List<FoodDoseSchedule>?> = _foodDoseSchedules

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

    fun loadFoodDoseSchedules(foodId: Long) = repository.getFoodDoseSchedules(foodId).asLiveData()

    fun remFoodDoseSchedules(viewLifeCycleOwner: LifecycleOwner, foodId: Long) {
        loadFoodDoseSchedules(foodId).observe(viewLifeCycleOwner) {
            it?.forEach { schedule ->
                remFoodDoseSchedule(schedule)
            }
        }
    }
}