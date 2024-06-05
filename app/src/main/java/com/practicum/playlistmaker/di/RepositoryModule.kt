package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.medialibrary.data.FavoritesRepositoryImpl
import com.practicum.playlistmaker.medialibrary.data.PlaylistsRepositoryImpl
import com.practicum.playlistmaker.medialibrary.domain.db.FavoritesRepository
import com.practicum.playlistmaker.medialibrary.domain.db.PlaylistsRepository
import com.practicum.playlistmaker.search.data.TracksRepositoryImpl
import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.settings.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import org.koin.dsl.module

    val repositoryModule = module {

        single<SettingsRepository> {
            SettingsRepositoryImpl(get(), get())
        }

        single<TracksRepository> {
            TracksRepositoryImpl(get(), get(), get())
        }

        single<FavoritesRepository> {
            FavoritesRepositoryImpl(get())
        }

        single<PlaylistsRepository> {
            PlaylistsRepositoryImpl(get())
        }
    }
