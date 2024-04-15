package com.practicum.playlistmaker.main.data

import com.practicum.playlistmaker.main.domain.MainRepository
import android.content.Context
import android.content.Intent
import com.practicum.playlistmaker.medialibrary.MLFragment

import com.practicum.playlistmaker.settings.ui.SettingsFragment

class MainRepositoryImpl(private val context: Context) : MainRepository {

    override fun navigateToSearch() {

    }

    override fun navigateToMediaLib() {
        val intent = Intent(context, MLFragment::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun navigateToSettings() {
        val intent = Intent(context, SettingsFragment::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
