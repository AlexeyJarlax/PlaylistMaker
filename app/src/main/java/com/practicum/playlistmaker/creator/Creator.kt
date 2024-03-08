package com.practicum.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.main.data.MainRepositoryImpl
import com.practicum.playlistmaker.main.domain.MainInteractor
import com.practicum.playlistmaker.main.domain.MainInteractorImpl
import com.practicum.playlistmaker.settings.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.SettingsInteractorImpl
import com.practicum.playlistmaker.settings.data.CommunicationButtonsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.CommunicationButtonsInteractor
import com.practicum.playlistmaker.settings.domain.CommunicationButtonsInteractorImpl
import com.practicum.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.MediaPlayerInteractor
import com.practicum.playlistmaker.player.domain.MediaPlayerInteractorImpl
import com.practicum.playlistmaker.search.data.RepositoryImplForHistoryTrack
import com.practicum.playlistmaker.search.data.RepositoryImplForTracksList
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClientForTracksList
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.TracksInteractorImpl
import com.practicum.playlistmaker.utils.AppPreferencesKeys

object Creator {

    fun provideMainInteractor(context: Context): MainInteractor {
        return MainInteractorImpl(
            mainRepository = MainRepositoryImpl(context)
        )
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        val sharedPreferences = provideSharedPreferences(context)
        return SettingsInteractorImpl(
            settingsRepository = SettingsRepositoryImpl(sharedPreferences)
        )
    }

    fun provideTracksInteractor(context: Context): TracksInteractor {
        val sp = provideSharedPreferences(context)
        return TracksInteractorImpl(
            repository = RepositoryImplForTracksList(RetrofitNetworkClientForTracksList()),
            history = RepositoryImplForHistoryTrack(sp)
        )
    }

    fun provideMediaPlayerInteractor(url: String): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(
            mediaPlayer = MediaPlayerRepositoryImpl(url)
        )
    }

    fun provideCommunicationButtonsInteractor(context: Context): CommunicationButtonsInteractor {
        return CommunicationButtonsInteractorImpl(
            communicationButtonsData = CommunicationButtonsRepositoryImpl(context)
        )
    }

    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(AppPreferencesKeys.PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
    }

}