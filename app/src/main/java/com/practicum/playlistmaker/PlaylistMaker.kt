package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.domain.api.RepositoryForSelectedTrack
import com.practicum.playlistmaker.data.RepositoryImplForSelectedTrack
import com.practicum.playlistmaker.domain.api.ProviderForSelectedTrack

class PlaylistMaker : Application(), ProviderForSelectedTrack {
    private lateinit var trackUseCase: RepositoryForSelectedTrack

    override fun onCreate() {
        super.onCreate()
        trackUseCase = RepositoryImplForSelectedTrack()
    }

    override fun provideTrackUseCase(): RepositoryForSelectedTrack {
        return trackUseCase
    }
}