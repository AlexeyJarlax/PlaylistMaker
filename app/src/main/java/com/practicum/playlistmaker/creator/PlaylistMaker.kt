package com.practicum.playlistmaker.creator
//
import android.app.Application

class PlaylistMaker : Application()  {
//    private lateinit var trackUseCase: RepositoryForSelectedTrack

    override fun onCreate() {
        super.onCreate()
//        trackUseCase = RepositoryImplForSelectedTrack()
    }

//    override fun provideTrackUseCase(): RepositoryForSelectedTrack {
//        return trackUseCase
//    }
}