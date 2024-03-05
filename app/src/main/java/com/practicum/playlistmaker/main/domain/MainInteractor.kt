package com.practicum.playlistmaker.main.domain

interface MainInteractor {
    fun loadNightMode(): Boolean
    fun navigateToSearch()
    fun navigateToMediaLib()
    fun navigateToSettings()
}