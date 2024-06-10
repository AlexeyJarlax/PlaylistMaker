package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.medialibrary.domain.impl.PlaylistInteractorImpl
import com.practicum.playlistmaker.medialibrary.domain.others.PlaylistsInteractor
import com.practicum.playlistmaker.medialibrary.domain.impl.FavoritesTracksInteractorImpl
import com.practicum.playlistmaker.medialibrary.domain.others.FavoritesTracksInteractor
import com.practicum.playlistmaker.player.domain.MediaPlayerInteractor
import com.practicum.playlistmaker.player.domain.MediaPlayerInteractorImpl
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.TracksInteractorImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.SettingsInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    factory<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(get())
    }

    factory<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    factory<FavoritesTracksInteractor> {
        FavoritesTracksInteractorImpl(repository = get())
    }

    factory <PlaylistsInteractor>{
        PlaylistInteractorImpl(repository = get(), imageStorage = get())
    }
}

