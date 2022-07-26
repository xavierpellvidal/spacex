package com.spacex.usecases.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacex.model.data.Launch
import com.spacex.model.data.Query
import com.spacex.model.data.QueryObject
import com.spacex.model.data.Rocket
import com.spacex.model.providers.LaunchesProvider
import com.spacex.model.providers.RocketsProvider
import com.spacex.model.providers.responses.LaunchesResponse
import com.spacex.model.providers.responses.RocketResponse
import com.spacex.util.NetworkError
import kotlinx.coroutines.launch

class RocketViewModel : ViewModel() {
    //Providers
    private var rocketsProvider = RocketsProvider()
    private var launchesProvider = LaunchesProvider()

    // Public constants
    val rocket: MutableLiveData<Rocket> = MutableLiveData()
    val launches: MutableLiveData<MutableList<Launch>> = MutableLiveData()
    val loader: MutableLiveData<Boolean> = MutableLiveData()
    val rocketError: MutableLiveData<NetworkError> = MutableLiveData()
    val launchesError: MutableLiveData<NetworkError> = MutableLiveData()

    // View Model
    fun loadRocket(id: String) {
        /* Load rocket coroutine */
        viewModelScope.launch {
            // Start data loading
            loader.postValue(true)

            //Load rocket info
            loadData(id)

            // End data loading
            loader.postValue(false)
        }
    }

    private suspend fun loadData(id: String) {
        // Load rocket info
        when (val response = rocketsProvider.getRocket(id)) {
            is RocketResponse.Success -> {
                // Post value to view
                rocket.postValue(response.data)
            }
            is RocketResponse.NoRocket -> rocketError.postValue(NetworkError.NULL_EMPTY_DATA)
            is RocketResponse.NoNetwork -> rocketError.postValue(NetworkError.NO_NETWORK)
            is RocketResponse.GenericError -> rocketError.postValue(NetworkError.GENERIC_ERROR)
        }

        // Load rocket launches with Query
        when (val response = launchesProvider.getLaunches(Query(QueryObject(rocket = id)))) {
            is LaunchesResponse.Success -> {
                // Post value to view
                launches.postValue(response.data)
            }
            is LaunchesResponse.NoLaunches -> launchesError.postValue(NetworkError.NULL_EMPTY_DATA)
            is LaunchesResponse.NoNetwork -> launchesError.postValue(NetworkError.NO_NETWORK)
            is LaunchesResponse.GenericError -> launchesError.postValue(NetworkError.GENERIC_ERROR)
        }
    }
}