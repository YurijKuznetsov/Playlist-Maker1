package com.example.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
const val SHARED_PREFERENCES = "shared_preferences"
const val SWITCHER_KEY = "switcher_key"
const val SEARCH_HISTORY_KEY = "search_history_key"
class App : Application() {
    object PrefsProvider {
        lateinit var sharedPreferences: SharedPreferences

        fun init(context: Context) {
            sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        }
    }

    private var darkTheme = false
    override fun onCreate() {
        super.onCreate()
        PrefsProvider.init(this)
        darkTheme = PrefsProvider.sharedPreferences.getBoolean(SWITCHER_KEY, false)
        switchTheme(darkTheme)

    }

    fun switchTheme(darkThemeEnabled : Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}