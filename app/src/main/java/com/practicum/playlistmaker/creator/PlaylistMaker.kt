package com.practicum.playlistmaker.creator

import android.app.Application
import org.koin.android.ext.koin.androidContext
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.interactorModule
import com.practicum.playlistmaker.di.repositoryModule
import com.practicum.playlistmaker.di.viewModelModule
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import org.koin.android.ext.android.inject
import org.koin.core.context.startKoin

class PlaylistMaker : Application()  {

    override fun onCreate() {
        super.onCreate()
        applyDayNightTheme()
        runKoinDependencies()
    }

    private fun runKoinDependencies() {
        startKoin {
            androidContext(this@PlaylistMaker)
            modules(dataModule, interactorModule, repositoryModule, viewModelModule)
        }}

    private fun applyDayNightTheme() {
        val settingsViewModel: SettingsInteractor by inject()
        settingsViewModel.applyTheme()
    }
}