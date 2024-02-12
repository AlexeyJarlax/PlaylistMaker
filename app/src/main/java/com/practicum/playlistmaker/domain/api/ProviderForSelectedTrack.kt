package com.practicum.playlistmaker.domain.api

// для ПлейАктивити

interface ProviderForSelectedTrack {
    fun provideTrackUseCase(): RepositoryForSelectedTrack
}