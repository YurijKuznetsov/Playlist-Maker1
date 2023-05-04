package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonSearch = findViewById<Button>(R.id.search)
        val buttonMedia = findViewById<Button>(R.id.mediateka)
        val buttonSetting = findViewById<Button>(R.id.setting)
        val searchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                Toast.makeText(this@MainActivity, "нажали на Поиск", Toast.LENGTH_SHORT).show()
            }
        }
        buttonSearch.setOnClickListener(searchClickListener)
        buttonMedia.setOnClickListener {
            Toast.makeText(this, "нажали на Медиатеку", Toast.LENGTH_SHORT).show()
        }
        buttonSetting.setOnClickListener {
            Toast.makeText(this, "нажали на Настройки", Toast.LENGTH_SHORT).show()
        }
    }
}