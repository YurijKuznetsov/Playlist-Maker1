package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
class  SearchHistory (sharedPrefs: SharedPreferences) {
    private val sharedPreferences = sharedPrefs
    val trackHistory = mutableListOf<Track>()
    fun read(): Tracks {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
        return Gson().fromJson(json, Tracks::class.java)
    }

    fun write(tracks: Tracks) {
        val json = Gson().toJson(tracks)
        sharedPreferences
            .edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }

    fun clear() {
        sharedPreferences
            .edit()
            .remove(SEARCH_HISTORY_KEY)
            .apply()
    }

}

data class Tracks (val tracks: List<Track>)
