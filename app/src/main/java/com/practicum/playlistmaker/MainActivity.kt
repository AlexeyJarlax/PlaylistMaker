package com.practicum.playlistmaker

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.button_search)
        val buttonMedialib = findViewById<Button>(R.id.button_media_lib)
        val buttonSettings = findViewById<Button>(R.id.button_settings)

        buttonSearch.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // для Android (API 24) и выше
                val displayIntent = Intent(this, SearchActivity::class.java)
                startActivity(displayIntent)
            } else {
                Toast.makeText(this, "Недоступно на версии Android ниже API 24", Toast.LENGTH_SHORT).show()
            }
        }

        buttonMedialib.setOnClickListener {
            val displayIntent = Intent(this, MedialabActivity::class.java)
            startActivity(displayIntent)
        }

        buttonSettings.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }
    }
}