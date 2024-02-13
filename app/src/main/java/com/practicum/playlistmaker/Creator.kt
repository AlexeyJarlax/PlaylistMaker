package com.practicum.playlistmaker

import com.practicum.playlistmaker.data.RepositoryImplForTracksList
import com.practicum.playlistmaker.data.network.RetrofitNetworkClientForTracksList
import com.practicum.playlistmaker.domain.api.InteractorForTracksList
import com.practicum.playlistmaker.domain.api.RepositoryForTracksList
import com.practicum.playlistmaker.domain.impl.InteractorImplForTracksList

object Creator {
    private fun getTrackRepository(): RepositoryForTracksList {
        return RepositoryImplForTracksList(RetrofitNetworkClientForTracksList())
    }

    fun provideTrackInteractor(): InteractorForTracksList {
        return InteractorImplForTracksList(getTrackRepository())
    }
}