package com.spacex.ui.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacex.data.model.Rocket
import com.spacex.UseCaseResponse
import com.spacex.ui.main.domain.GetFilteredRockets
import com.spacex.ui.main.domain.GetRockets
import com.spacex.ViewModelResponse
import kotlinx.coroutines.launch

class RocketListViewModel() : ViewModel() {
    // Provider
    private var rocketsUseCase = GetRockets()
    private var filteredRocketsUseCase = GetFilteredRockets()

    // Public vars
    var rockets: MutableLiveData<MutableList<Rocket>> = MutableLiveData()
    val loader: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<ViewModelResponse> = MutableLiveData()


    // Get rocket list function
    fun getRocketsList(enabledFilters: Boolean, activeFilter: Boolean) {
        /* Load data coroutine */
        viewModelScope.launch {
            // Start data loading
            loader.postValue(true)

            if(enabledFilters)
                loadFilteredData(activeFilter)
            else
                loadData()

            // Hide loader
            loader.postValue(false)
        }
    }

    private suspend fun loadData() {
        // Load rockets from provider
        when (val response = rocketsUseCase.getRockets()) {
            is UseCaseResponse.Success -> {
                // Post value to view
                rockets.postValue(response.data)
            }
            is UseCaseResponse.NoData -> error.postValue(ViewModelResponse.NULL_EMPTY_DATA)
            is UseCaseResponse.NoNetwork -> error.postValue(ViewModelResponse.NO_NETWORK)
            is UseCaseResponse.GenericError -> error.postValue(ViewModelResponse.GENERIC_ERROR)
        }
    }

    private suspend fun loadFilteredData(activeFilter: Boolean) {
        // Load rockets from provider
        when (val response = filteredRocketsUseCase.getFilteredRockets(activeFilter)) {
            is UseCaseResponse.Success -> {
                // Post value to view
                rockets.postValue(response.data)
            }
            is UseCaseResponse.NoData -> error.postValue(ViewModelResponse.NULL_EMPTY_DATA)
            is UseCaseResponse.NoNetwork -> error.postValue(ViewModelResponse.NO_NETWORK)
            is UseCaseResponse.GenericError -> error.postValue(ViewModelResponse.GENERIC_ERROR)
        }
    }
}