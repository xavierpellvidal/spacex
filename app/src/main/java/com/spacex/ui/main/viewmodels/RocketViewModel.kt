package com.spacex.ui.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacex.data.model.Launch
import com.spacex.data.model.Query
import com.spacex.data.model.QueryObject
import com.spacex.data.model.Rocket
import com.spacex.UseCaseResponse
import com.spacex.ui.main.domain.GetLaunches
import com.spacex.ui.main.domain.GetRocket
import com.spacex.ViewModelResponse
import kotlinx.coroutines.launch

class RocketViewModel : ViewModel() {
    //Providers
    private var rocketUseCase = GetRocket()
    private var launchesUseCase = GetLaunches()

    // Public constants
    val rocket: MutableLiveData<Rocket> = MutableLiveData()
    val launches: MutableLiveData<MutableList<Launch>> = MutableLiveData()
    val loader: MutableLiveData<Boolean> = MutableLiveData()
    val rocketError: MutableLiveData<ViewModelResponse> = MutableLiveData()
    val launchesError: MutableLiveData<ViewModelResponse> = MutableLiveData()

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
        when (val response = rocketUseCase.getRocket(id)) {
            is UseCaseResponse.Success -> {
                // Post value to view
                rocket.postValue(response.data)
            }
            is UseCaseResponse.NoData -> rocketError.postValue(ViewModelResponse.NULL_EMPTY_DATA)
            is UseCaseResponse.NoNetwork -> rocketError.postValue(ViewModelResponse.NO_NETWORK)
            is UseCaseResponse.GenericError -> rocketError.postValue(ViewModelResponse.GENERIC_ERROR)
        }

        // Load rocket launches with Query
        when (val response = launchesUseCase.getLaunches(Query(QueryObject(rocket = id)))) {
            is UseCaseResponse.Success -> {
                // Post value to view
                launches.postValue(response.data)
            }
            is UseCaseResponse.NoData -> launchesError.postValue(ViewModelResponse.NULL_EMPTY_DATA)
            is UseCaseResponse.NoNetwork -> launchesError.postValue(ViewModelResponse.NO_NETWORK)
            is UseCaseResponse.GenericError -> launchesError.postValue(ViewModelResponse.GENERIC_ERROR)
        }
    }
}