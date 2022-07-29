package com.spacex.ui.main.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacex.data.model.Rocket
import com.spacex.UseCaseResponse
import com.spacex.ui.main.domain.GetFilteredRockets
import com.spacex.ui.main.domain.GetRockets
import com.spacex.ViewModelResponse
import kotlinx.coroutines.launch

class RocketListViewModel : ViewModel() {
    // Domain use cases
    private var rocketsUseCase = GetRockets()
    private var filteredRocketsUseCase = GetFilteredRockets()

    // Livedata
    private val _rockets: MutableLiveData<MutableList<Rocket>> = MutableLiveData()
    val rockets: LiveData<MutableList<Rocket>> = _rockets
    private val _loader: MutableLiveData<Boolean> = MutableLiveData()
    val loader: LiveData<Boolean> = _loader
    private val _error: MutableLiveData<ViewModelResponse> = MutableLiveData()
    val error: LiveData<ViewModelResponse> = _error

    // Get rocket list function
    fun getRocketsList(enabledFilters: Boolean, activeFilter: Boolean) {
        /* Load data coroutine */
        viewModelScope.launch {
            // Start data loading
            _loader.postValue(true)

            if(enabledFilters)
                loadFilteredData(activeFilter)
            else
                loadData()

            // Hide loader
            _loader.postValue(false)
        }
    }

    private suspend fun loadData() {
        // Load rockets from provider
        when (val response = rocketsUseCase.getRockets()) {
            is UseCaseResponse.Success -> {
                // Post value to view
                _rockets.postValue(response.data)
            }
            is UseCaseResponse.NoData -> _error.postValue(ViewModelResponse.NULL_EMPTY_DATA)
            is UseCaseResponse.NoNetwork -> _error.postValue(ViewModelResponse.NO_NETWORK)
            is UseCaseResponse.GenericError -> _error.postValue(ViewModelResponse.GENERIC_ERROR)
        }
    }

    private suspend fun loadFilteredData(activeFilter: Boolean) {
        // Load rockets from provider
        when (val response = filteredRocketsUseCase.getFilteredRockets(activeFilter)) {
            is UseCaseResponse.Success -> {
                // Post value to view
                _rockets.postValue(response.data)
            }
            is UseCaseResponse.NoData -> _error.postValue(ViewModelResponse.NULL_EMPTY_DATA)
            is UseCaseResponse.NoNetwork -> _error.postValue(ViewModelResponse.NO_NETWORK)
            is UseCaseResponse.GenericError -> _error.postValue(ViewModelResponse.GENERIC_ERROR)
        }
    }
}