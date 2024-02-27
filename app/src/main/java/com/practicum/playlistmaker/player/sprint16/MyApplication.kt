package com.practicum.playlistmaker.player.sprint16

import android.app.Application

class MyApplication: Application() {
    fun getRepository(): TracksRepositoryImpl {
        return TracksRepositoryImpl(NetworkClientImpl())
    }

    fun provideTracksInteractor(): TrackInteractor {
        return TracksInteractorImpl(getRepository())
    }
}