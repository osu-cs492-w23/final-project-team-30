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

    private val _searchLoadingStatus = MutableLiveData(LoadingStatus.SUCCESS)
    val searchLoadingStatus: LiveData<LoadingStatus> = _searchLoadingStatus

    private val _detailsLoadingStatus = MutableLiveData(LoadingStatus.ERROR)
    val detailsLoadingStatus: LiveData<LoadingStatus> = _detailsLoadingStatus

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadSearchResults(appId: String, appKey: String, ingredient: String) {
        viewModelScope.launch {
            _searchLoadingStatus.value = LoadingStatus.LOADING

            val result = repository.loadAllergenSearch(appId, appKey, ingredient)
            _searchResults.value = result.getOrNull()

            _searchLoadingStatus.value = when (result.isSuccess) {
                true -> LoadingStatus.SUCCESS
                false -> LoadingStatus.ERROR
            }

            if (result.isFailure)
                _errorMessage.value = result.exceptionOrNull()?.message ?: "unknown error"
        }
    }

    fun loadAllergenDetails(appId: String, appKey: String, foodResult: FoodResult) {
        viewModelScope.launch {
            _detailsLoadingStatus.value = LoadingStatus.LOADING
            val result = repository.loadFoodDetails(appId, appKey, foodResult)

            _foodDetails.value = result.getOrNull()

            _detailsLoadingStatus.value = when (result.isSuccess) {
                true -> LoadingStatus.SUCCESS
                false -> LoadingStatus.ERROR
            }

            if (result.isFailure)
                _errorMessage.value = result.exceptionOrNull()?.message ?: "unknown error"
        }
    }
}