package com.practicum.playlistmaker

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
        runKoinDependencies()
        applyDayNightTheme()
    }

    private fun runKoinDependencies() {
        startKoin {
            androidContext(this@PlaylistMaker)
            modules(dataModule, interactorModule, repositoryModule, viewModelModule)
        }}

    private fun applyDayNightTheme() {
        val settings: SettingsInteractor by inject()
        settings.applyTheme()
    }
}