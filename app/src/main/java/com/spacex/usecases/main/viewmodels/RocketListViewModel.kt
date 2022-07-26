package com.spacex.usecases.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacex.model.data.Rocket
import com.spacex.model.providers.RocketsProvider
import com.spacex.model.providers.responses.RocketsListResponse
import com.spacex.util.NetworkError
import kotlinx.coroutines.launch

class RocketListViewModel : ViewModel() {
    // Provider
    private var rocketsProvider = RocketsProvider()

    // Public vars
    var rockets: MutableLiveData<MutableList<Rocket>> = MutableLiveData()
    val loader: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<NetworkError> = MutableLiveData()

    //Init view model
    init {
        getRocketsList()
    }

    // Get rocket list function
    fun getRocketsList() {
        /* Load data coroutine */
        viewModelScope.launch {
            // Start data loading
            loader.postValue(true)

            // Load info
            loadData()

            // Hide loader
            loader.postValue(false)
        }
    }

    private suspend fun loadData() {
        // Load rockets from provider
        when (val response = rocketsProvider.getRockets()) {
            is RocketsListResponse.Success -> {
                // Post value to view
                rockets.postValue(response.data)
            }
            is RocketsListResponse.NoRockets -> error.postValue(NetworkError.NULL_EMPTY_DATA)
            is RocketsListResponse.NoNetwork -> error.postValue(NetworkError.NO_NETWORK)
            is RocketsListResponse.GenericError -> error.postValue(NetworkError.GENERIC_ERROR)
        }
    }
}