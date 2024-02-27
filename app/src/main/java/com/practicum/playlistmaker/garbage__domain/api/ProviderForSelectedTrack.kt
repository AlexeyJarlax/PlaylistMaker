package com.practicum.playlistmaker.garbage__domain.api

// для ПлейАктивити

interface ProviderForSelectedTrack {
    fun provideTrackUseCase(): RepositoryForSelectedTrack
}