package com.practicum.playlistmaker.main.domain

class MainInteractorImpl(private val mainRepository: MainRepository) :
    MainInteractor {

    override fun loadNightMode(): Boolean {
        return mainRepository.loadNightMode()
    }

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