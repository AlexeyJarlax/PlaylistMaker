package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        val back = findViewById<Button>(R.id.button_back_from_settings) // КНОПКА НАЗАД
        back.setOnClickListener {
            finish()
        }

        // КНОПКА НОЧНОЙ И ДНЕВНОЙ ТЕМЫ
        val switchDarkMode: androidx.appcompat.widget.SwitchCompat =
            findViewById(R.id.switch_dark_mode)
        switchDarkMode.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                setNightMode(true)
            } else {
                setNightMode(false)
            }
        }

        // КНОПКА ПОДЕЛИТЬСЯ
        val shareButton = findViewById<Button>(R.id.button_settings_share)
        shareButton.setOnClickListener {
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
        helpButton.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse(getString(R.string.support_email))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_email_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.support_email_text))
                startActivity(this)
            }
        }

        // КНОПКА ПОЛЬЗОВАТЕЛЬСКОГО СОГЛАШЕНИЯ
        val userAgreementButton = findViewById<Button>(R.id.button_settings_user_agreement)
        userAgreementButton.setOnClickListener {
            val url = getString(R.string.user_agreement_url)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }

    // ИСПОЛНИТЕЛЬ КНОПКИ НОЧНОЙ И ДНЕВНОЙ ТЕМЫ
    private fun setNightMode(enabled: Boolean) {
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> if (!enabled) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                recreateActivity()
            }

            Configuration.UI_MODE_NIGHT_NO -> if (enabled) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                recreateActivity()
            }
        }
    }

    private fun recreateActivity() {
        recreate()
    }
}