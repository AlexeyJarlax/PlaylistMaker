package com.practicum.playlistmaker

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.practicum.playlistmaker.util.UtilThemeManager
import com.practicum.playlistmaker.util.setDebouncedClickListener

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UtilThemeManager.applyTheme(this) // Применяю тему сразу при запуске 12 СПРИНТ
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.button_search)
        val buttonMedialib = findViewById<Button>(R.id.button_media_lib)
        val buttonSettings = findViewById<Button>(R.id.button_settings)

        buttonSearch.setDebouncedClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // для Android (API 24) и выше
                val displayIntent = Intent(this, SearchActivity::class.java)
                startActivity(displayIntent)
            } else {
                Toast.makeText(this, "Недоступно на версии Android ниже API 24", Toast.LENGTH_SHORT).show()
            }
        }

        buttonMedialib.setDebouncedClickListener {
            val displayIntent = Intent(this, MedialabActivity::class.java)
            startActivity(displayIntent)
        }

        buttonSettings.setDebouncedClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }
    }
}