package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.main.data.MainRepositoryImpl
import com.practicum.playlistmaker.main.domain.MainRepository
import com.practicum.playlistmaker.search.data.TracksRepositoryImpl
import com.practicum.playlistmaker.search.domain.TracksRepository
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

        single<TracksRepository> {
            TracksRepositoryImpl(get(), get(), get())
        }
    }
