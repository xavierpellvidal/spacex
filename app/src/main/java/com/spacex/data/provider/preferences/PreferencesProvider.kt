package com.spacex.data.repository.preferences

import android.content.Context
import android.content.SharedPreferences

enum class PreferencesKey {
    SPACEX_PREFERENCES,
    ROCKETS_FILTER_ENABLED,
    ROCKETS_ACTIVE_FILTER
}

class PreferencesProvider private constructor(context: Context) {

    companion object {
        // SharedPreference singleton
        private var instance: PreferencesProvider? = null

        fun getInstance(context: Context): PreferencesProvider {
            if (instance == null) {
                instance = PreferencesProvider(context)
            }
            return instance!!
        }
    }

    private val mPrefs: SharedPreferences by lazy {
        context.getSharedPreferences(PreferencesKey.SPACEX_PREFERENCES.name, Context.MODE_PRIVATE)
    }

    fun set(key: PreferencesKey, value: String?) {
        val editor = mPrefs.edit()
        editor.putString(key.name, value).apply()
    }

    fun string(key: PreferencesKey): String? {
        return mPrefs.getString(key.name, null)
    }

    // Delete sharedPrefs
    fun clear() {
        val editor = mPrefs.edit()
        editor.clear().apply()
    }

    var filtersEnabled: Boolean
        get() = mPrefs.getBoolean(PreferencesKey.ROCKETS_FILTER_ENABLED.name, false)
        set(value) {
            val editor = mPrefs.edit()
            editor.putBoolean(PreferencesKey.ROCKETS_FILTER_ENABLED.name, value).apply()
        }

    var activeFilterValue: Boolean
        get() = mPrefs.getBoolean(PreferencesKey.ROCKETS_ACTIVE_FILTER.name, false)
        set(value) {
            val editor = mPrefs.edit()
            editor.putBoolean(PreferencesKey.ROCKETS_ACTIVE_FILTER.name, value).apply()
        }
}