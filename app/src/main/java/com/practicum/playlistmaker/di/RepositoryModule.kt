package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.main.data.MainRepositoryImpl
import com.practicum.playlistmaker.main.domain.MainRepository
import com.practicum.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.MediaPlayerRepository
import com.practicum.playlistmaker.search.data.RepositoryImplForHistoryTrack
import com.practicum.playlistmaker.search.data.RepositoryImplForTracksList
import com.practicum.playlistmaker.search.domain.TracksHistoryRepository
import com.practicum.playlistmaker.search.domain.TracksSearchRepository
import com.practicum.playlistmaker.settings.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import org.koin.dsl.module

    val repositoryModule = module {

        single<MainRepository> {
            MainRepositoryImpl(get())
        }

        single<SettingsRepository> {
            SettingsRepositoryImpl(get(), get())
        }

        single<TracksHistoryRepository> {
            RepositoryImplForHistoryTrack(get())
        }

        single<TracksSearchRepository> {
            RepositoryImplForTracksList(get())
        }

//        single<MediaPlayerRepository> {
//            MediaPlayerRepositoryImpl(get())
//        }
    }
