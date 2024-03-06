package com.practicum.playlistmaker.main.domain

class MainInteractorImpl(private val mainRepository: MainRepository) :
    MainInteractor {

    override fun navigateToSearch() {
        mainRepository.navigateToSearch()
    }
    override fun navigateToMediaLib() {
        mainRepository.navigateToMediaLib()
    }
    override fun navigateToSettings() {
        mainRepository.navigateToSettings()
    }
}