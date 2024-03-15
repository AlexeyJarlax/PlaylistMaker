package com.practicum.playlistmaker.settings.data

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.utils.AppPreferencesKeys.KEY_NIGHT_MODE

class SettingsRepositoryImpl(private val context: Context, private val sharedPreferences: SharedPreferences) :
    SettingsRepository {

    override fun loadNightMode(): Boolean {
        return sharedPreferences.getBoolean(KEY_NIGHT_MODE, false)
    }

    override fun saveNightMode(value: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_NIGHT_MODE, value).apply()
    }

    override fun applyTheme() { // функция, применяющая настройки день/ночь на все приложение, передаю в Application()
        val nightModeEnabled = loadNightMode()
        if (nightModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun buttonToShareApp() {
        val appId = ""
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(
            Intent.EXTRA_TEXT,
            context.getString(R.string.share_app_text, appId)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val chooserIntent = Intent.createChooser(intent, context.getString(R.string.share_app_title))
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooserIntent)
    }

    override fun buttonToHelp() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse(context.getString(R.string.support_email))
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.support_email_subject))
        intent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.support_email_text))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun buttonToSeeUserAgreement() {
        val url = context.getString(R.string.user_agreement_url)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
