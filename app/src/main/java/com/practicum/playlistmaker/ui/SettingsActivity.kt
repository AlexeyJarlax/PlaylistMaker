package com.practicum.playlistmaker.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.network.CommunicationButtons
import com.practicum.playlistmaker.presentation.UtilThemeManager
import com.practicum.playlistmaker.domain.impl.setDebouncedClickListener
import com.practicum.playlistmaker.presentation.buttonBack

class SettingsActivity : AppCompatActivity() {


    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UtilThemeManager.applyTheme(this)
        setContentView(R.layout.activity_settings)
        buttonBack()

        // КНОПКА НОЧНОЙ И ДНЕВНОЙ ТЕМЫ (РЕАЛИЗАЦИЯ ВЫНЕСЕНА В UtilThemeManager)
        val switchDarkMode: SwitchCompat = findViewById(R.id.switch_dark_mode)
        switchDarkMode.isChecked = UtilThemeManager.isNightModeEnabled(this)
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            UtilThemeManager.setNightModeEnabled(this, isChecked)
            UtilThemeManager.applyTheme(this)
        }

        // КНОПКА ПОДЕЛИТЬСЯ
        val shareButton = findViewById<Button>(R.id.button_settings_share)
        shareButton.setDebouncedClickListener {
            CommunicationButtons(this).buttonShare()
        }

        // КНОПКА ТЕХПОДДЕРЖКИ
        val helpButton = findViewById<Button>(R.id.button_settings_write_to_supp)
        helpButton.setDebouncedClickListener {
            CommunicationButtons(this).buttonHelp()
        }

        // КНОПКА ПОЛЬЗОВАТЕЛЬСКОГО СОГЛАШЕНИЯ
        val userAgreementButton = findViewById<Button>(R.id.button_settings_user_agreement)
        userAgreementButton.setDebouncedClickListener {
            val url = getString(R.string.user_agreement_url)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }
}