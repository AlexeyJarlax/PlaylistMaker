package com.practicum.playlistmaker.main.domain

interface MainRepository {
    fun loadNightMode(): Boolean
    fun navigateToSearch()
    fun navigateToMediaLib()
    fun navigateToSettings()
}