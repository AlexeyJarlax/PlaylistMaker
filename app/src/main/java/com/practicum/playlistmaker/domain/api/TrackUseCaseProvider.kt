package com.practicum.playlistmaker.domain.api

// для ПлейАктивити

interface TrackUseCaseProvider {
    fun provideTrackUseCase(): TrackUseCase
}