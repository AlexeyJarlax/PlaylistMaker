package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.main.domain.MainInteractor
import com.practicum.playlistmaker.main.domain.MainInteractorImpl
import com.practicum.playlistmaker.player.domain.MediaPlayerInteractor
import com.practicum.playlistmaker.player.domain.MediaPlayerInteractorImpl
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.TracksInteractorImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.SettingsInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory<MainInteractor> {
        MainInteractorImpl(get())
    }

    factory<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    factory<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(get())
    }

    factory<TracksInteractor> {
        TracksInteractorImpl(get())
    }
}

