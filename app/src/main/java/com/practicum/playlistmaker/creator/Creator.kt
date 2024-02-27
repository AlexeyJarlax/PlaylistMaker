package com.practicum.playlistmaker.creator

import com.practicum.playlistmaker.garbage__data.RepositoryImplForTracksList
import com.practicum.playlistmaker.garbage__data.network.RetrofitNetworkClientForTracksList
import com.practicum.playlistmaker.garbage__domain.api.InteractorForTracksList
import com.practicum.playlistmaker.garbage__domain.api.RepositoryForTracksList
import com.practicum.playlistmaker.garbage__domain.impl.InteractorImplForTracksList

object Creator {
    //для сёрчАктивити
    private fun getTrackRepository(): RepositoryForTracksList {
        return RepositoryImplForTracksList(RetrofitNetworkClientForTracksList())
    }
    fun provideTrackInteractor(): InteractorForTracksList {
        return InteractorImplForTracksList(getTrackRepository())
    }

    // для плейАкивити
//    fun getRepository(): TracksRepositoryImpl {
//        return TracksRepositoryImpl(NetworkClientImpl())
//    }
//    fun provideTracksInteractor(): TrackInteractor {
//        return TracksInteractorImpl(getRepository())
//    }
}