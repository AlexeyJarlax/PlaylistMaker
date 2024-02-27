package com.practicum.playlistmaker.creator
//
import android.app.Application
import com.practicum.playlistmaker.garbage__domain.api.RepositoryForSelectedTrack
import com.practicum.playlistmaker.garbage__data.RepositoryImplForSelectedTrack
import com.practicum.playlistmaker.garbage__domain.api.ProviderForSelectedTrack

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