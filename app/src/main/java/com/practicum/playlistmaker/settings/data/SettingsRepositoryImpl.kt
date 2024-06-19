package com.practicum.playlistmaker.settings.data

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.utils.AppPreferencesKeys.KEY_NIGHT_MODE
import de.cketti.mailto.EmailIntentBuilder

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
        val appId = context.getString(R.string.app_id)
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(
            Intent.EXTRA_TEXT,
            context.getString(R.string.share_app_text) + appId // идентификатор приложения
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val chooserIntent = Intent.createChooser(intent, context.getString(R.string.share_app_title))
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooserIntent)
    }

    override fun buttonToHelp() {
        val subject = context.getString(R.string.support_email_subject)
        val body = context.getString(R.string.support_email_text)
        val email = context.getString(R.string.support_email)
        val emailIntent = EmailIntentBuilder.from(context)
            .to(email)
            .subject(subject)
            .body(body)
            .start()
    }

    override fun buttonToSeeUserAgreement() {
        val url = context.getString(R.string.user_agreement_url)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun sharePlaylist(message: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, message)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
