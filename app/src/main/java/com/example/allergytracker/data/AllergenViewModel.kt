package com.example.allergytracker.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allergytracker.api.AllergenLookupService
import kotlinx.coroutines.launch

class AllergenViewModel : ViewModel() {
    private val repository = AllergenRepository(AllergenLookupService.create())

    private val _searchResults = MutableLiveData<List<FoodResult>?>(null)
    val searchResults: LiveData<List<FoodResult>?> = _searchResults

    private val _foodDetails = MutableLiveData<FoodAllergenDetails?>(null)
    val foodDetails: LiveData<FoodAllergenDetails?> = _foodDetails

    private val _loadingStatus = MutableLiveData<LoadingStatus>(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadSearchResults(appId: String, appKey: String, ingredient: String) {
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.LOADING

            val result = repository.loadAllergenSearch(appId, appKey, ingredient)
            _searchResults.value = result.getOrNull()

            _loadingStatus.value = when (result.isSuccess) {
                true -> LoadingStatus.SUCCESS
                false -> LoadingStatus.ERROR
            }

            if (result.isFailure)
                _errorMessage.value = result.exceptionOrNull()?.message
        }
    }

    fun loadAllergenDetails(appId: String, appKey: String, foodResult: FoodResult) {
        viewModelScope.launch {
            Log.d("Main", "Loading details for ${foodResult.name}")
            val result = repository.loadFoodDetails(appId, appKey, foodResult)
            Log.d("Main", "Retrieved values")
            _foodDetails.value = result.getOrNull()

            if (result.isFailure)
                Log.e("Main", result.exceptionOrNull().toString())
        }
    }
}