package com.practicum.playlistmaker.main.domain

import android.content.Context
import android.content.Intent
import com.practicum.playlistmaker.mediateka.MedialabActivity
import com.practicum.playlistmaker.search.ui.SearchActivity
import com.practicum.playlistmaker.settings.ui.SettingsActivity

class MainUseCase {

    fun navigateToSearch(context: Context) {
        val intent = Intent(context, SearchActivity::class.java)
        context.startActivity(intent)
    }

    fun navigateToMediaLib(context: Context) {
        val intent = Intent(context, MedialabActivity::class.java)
        context.startActivity(intent)
    }

    fun navigateToSettings(context: Context) {
        val intent = Intent(context, SettingsActivity::class.java)
        context.startActivity(intent)
    }
}