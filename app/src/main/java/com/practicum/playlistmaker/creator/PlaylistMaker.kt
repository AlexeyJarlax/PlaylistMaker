package com.practicum.playlistmaker.creator

import android.app.Application
import com.practicum.playlistmaker.settings.ui.SettingsViewModel

class PlaylistMaker : Application()  {

    override fun onCreate() {
        super.onCreate()
        val settingsViewModel = SettingsViewModel(
            settingsInteractor = Creator.provideSettingsInteractor(this),
            communicationButtonsInteractor = Creator.provideCommunicationButtonsInteractor(this)
        )
        settingsViewModel.applyTheme()
    }
}