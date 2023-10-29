package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val image1 = findViewById<Button>(R.id.button_search)
        val image2 = findViewById<Button>(R.id.button_media_lib)
        val image3 = findViewById<Button>(R.id.button_settings)

        image1.setOnClickListener {
            val displayIntent = Intent(this, SearchActivity::class.java)
            startActivity(displayIntent)
        }

        image2.setOnClickListener {
            val displayIntent = Intent(this, MedialabActivity::class.java)
            startActivity(displayIntent)
        }

        image3.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }
    }
}