package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonSearch = findViewById<Button>(R.id.search)
        val buttonMedia = findViewById<Button>(R.id.mediateka)
        val buttonSetting = findViewById<Button>(R.id.setting)
        buttonSearch.setOnClickListener {
            val displaySearch = Intent(this, SearchActivity::class.java)
            startActivity(displaySearch)
        }
        buttonMedia.setOnClickListener {
            val displayMediateka = Intent(this, MediatekaActivity::class.java)
            startActivity(displayMediateka)
        }
        buttonSetting.setOnClickListener {
            val displaySetting = Intent(this, SettingsActivity::class.java)
            startActivity(displaySetting)
        }
    }
}