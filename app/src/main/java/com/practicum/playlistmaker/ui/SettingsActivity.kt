package com.practicum.playlistmaker.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.UtilThemeManager
import com.practicum.playlistmaker.domain.impl.setDebouncedClickListener

class SettingsActivity : AppCompatActivity() {


    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UtilThemeManager.applyTheme(this)
        setContentView(R.layout.activity_settings)

        val back = findViewById<Button>(R.id.buttonBackFromSettings) // КНОПКА НАЗАД
        back.setDebouncedClickListener {
            finish()
        }

        // КНОПКА НОЧНОЙ И ДНЕВНОЙ ТЕМЫ (РЕАЛИЗАЦИЯ ВЫНЕСЕНА В UtilThemeManager)
        val switchDarkMode: SwitchCompat = findViewById(R.id.switch_dark_mode)
        switchDarkMode.isChecked = UtilThemeManager.isNightModeEnabled(this)
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            UtilThemeManager.setNightModeEnabled(this, isChecked)
        }


        // КНОПКА ПОДЕЛИТЬСЯ
        val shareButton = findViewById<Button>(R.id.button_settings_share)
        shareButton.setDebouncedClickListener {
            val appId = "com.Practicum.PlaylistMaker"
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.share_app_text, appId)
            )
            startActivity(Intent.createChooser(intent, getString(R.string.share_app_title)))
        }

        // КНОПКА ТЕХПОДДЕРЖКИ
        val helpButton = findViewById<Button>(R.id.button_settings_write_to_supp)
        helpButton.setDebouncedClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse(getString(R.string.support_email))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_email_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.support_email_text))
                startActivity(this)
            }
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