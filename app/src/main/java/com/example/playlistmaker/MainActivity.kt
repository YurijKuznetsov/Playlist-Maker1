package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonSearch = findViewById<Button>(R.id.buttonSearch)
        val buttonMedia = findViewById<Button>(R.id.buttonMediateka)
        val buttonSetting = findViewById<Button>(R.id.buttonSettings)
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