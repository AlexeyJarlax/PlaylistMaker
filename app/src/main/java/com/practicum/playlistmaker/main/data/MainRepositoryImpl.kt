package com.practicum.playlistmaker.main.data

import android.content.SharedPreferences
import com.practicum.playlistmaker.main.domain.MainRepository
import com.practicum.playlistmaker.utils.AppPreferencesKeys.KEY_NIGHT_MODE
import android.content.Context
import android.content.Intent
import com.practicum.playlistmaker.mediateka.MedialabActivity
import com.practicum.playlistmaker.search.ui.SearchActivity
import com.practicum.playlistmaker.settings.ui.SettingsActivity

class MainRepositoryImpl(private val context: Context, private val sharedPreferences: SharedPreferences) : MainRepository {
    override fun loadNightMode(): Boolean {
        return sharedPreferences.getBoolean(KEY_NIGHT_MODE, false)
    }

    override fun navigateToSearch() {
        val intent = Intent(context, SearchActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun navigateToMediaLib() {
        val intent = Intent(context, MedialabActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun navigateToSettings() {
        val intent = Intent(context, SettingsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
