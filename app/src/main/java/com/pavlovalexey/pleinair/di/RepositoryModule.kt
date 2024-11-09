package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.medialibrary.data.db.impl.FavoritesTrackRepositoryImpl
import com.practicum.playlistmaker.medialibrary.data.db.storage.ImageStorageImpl
import com.practicum.playlistmaker.medialibrary.data.db.impl.PlaylistsRepositoryImpl
import com.practicum.playlistmaker.medialibrary.domain.others.ImageStorage
import com.practicum.playlistmaker.medialibrary.domain.others.FavoritesTracksRepository
import com.practicum.playlistmaker.medialibrary.domain.others.PlaylistsRepository
import com.practicum.playlistmaker.search.data.TracksRepositoryImpl
import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.settings.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

    val repositoryModule = module {

        single<SettingsRepository> {
            SettingsRepositoryImpl(get(), get())
        }

        single<TracksRepository> {
            TracksRepositoryImpl(get(), get(), get())
        }

        single<FavoritesTracksRepository> {
            FavoritesTrackRepositoryImpl(get())
        }

        single<PlaylistsRepository> {
            PlaylistsRepositoryImpl(playlistDb = get(), converter = get())
        }

        single<ImageStorage> {
            ImageStorageImpl(context = androidContext())
        }
    }
