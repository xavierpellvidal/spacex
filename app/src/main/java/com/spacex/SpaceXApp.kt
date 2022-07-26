package com.spacex

import android.app.Application
import com.spacex.model.repository.api.v4.SpaceXAPI
import com.spacex.model.repository.preferences.PreferencesProvider

class SpaceXApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // This app
        instance = this

        // Shared prefs
        mPrefs = PreferencesProvider.getInstance(this)

        // Api Service
        apiService = SpaceXAPI.getInstance()
        apiService.initApiService()
    }

    companion object {
        lateinit var instance: SpaceXApp
            private set

        lateinit var mPrefs: PreferencesProvider
            private set

        lateinit var apiService: SpaceXAPI
            private set
    }
}