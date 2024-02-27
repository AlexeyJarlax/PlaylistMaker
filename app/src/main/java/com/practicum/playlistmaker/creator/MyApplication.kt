package com.practicum.playlistmaker.creator

import android.app.Application
import com.practicum.playlistmaker.search.data.TracksRepositoryImpl
import com.practicum.playlistmaker.search.domain.TracksInteractorImpl

class MyApplication: Application() {
//    fun getRepository(): TracksRepositoryImpl {
//        return TracksRepositoryImpl(NetworkClientImpl())
//    }
//
//    fun provideTracksInteractor(): TrackInteractor {
//        return TracksInteractorImpl(getRepository())
//    }
}