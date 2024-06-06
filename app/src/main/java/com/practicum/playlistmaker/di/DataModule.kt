package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.google.gson.Gson
import com.practicum.playlistmaker.medialibrary.data.FavoritesRepositoryImpl
import com.practicum.playlistmaker.medialibrary.data.db.AppDatabase
import com.practicum.playlistmaker.medialibrary.domain.db.FavoritesInteractor
import com.practicum.playlistmaker.medialibrary.domain.db.FavoritesInteractorImpl
import com.practicum.playlistmaker.medialibrary.domain.db.FavoritesRepository
import com.practicum.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.MediaPlayerRepository
import com.practicum.playlistmaker.search.data.network.ITunesAPIService
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.utils.AppPreferencesKeys
import org.koin.dsl.module
import retrofit2.converter.gson.GsonConverterFactory
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.settings.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.utils.AppPreferencesKeys.DATA_BASE_NAME
import org.koin.android.ext.koin.androidContext
import retrofit2.Retrofit

    val dataModule = module {

        single<ITunesAPIService> {
            Retrofit.Builder()
            .baseUrl(AppPreferencesKeys.iTunesSearchUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesAPIService::class.java)
        }

        single {
            androidContext()
                .getSharedPreferences(AppPreferencesKeys.PREFS_NAME, Context.MODE_PRIVATE)
        }

        single<SettingsRepository> {
            SettingsRepositoryImpl(get(), get())
        }

        single<NetworkClient> {
            RetrofitNetworkClient(get())
        }

        factory<MediaPlayerRepository> {
            MediaPlayerRepositoryImpl(get())
        }

        factory { Gson() }
        factory { MediaPlayer() }

        single {
            Room.databaseBuilder(
                androidContext(),
                AppDatabase::class.java, DATA_BASE_NAME
            ).fallbackToDestructiveMigration()
                .build()
        }
    }
