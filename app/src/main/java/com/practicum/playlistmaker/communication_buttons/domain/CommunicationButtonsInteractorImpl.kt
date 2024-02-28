package com.practicum.playlistmaker.communication_buttons.domain

class CommunicationButtonsInteractorImpl(private val communicationButtonsData: CommunicationButtonsData) : CommunicationButtonsInteractor {

    override fun buttonToShareApp() {
        communicationButtonsData.buttonToShareApp()
    }

    override fun buttonToHelp() {
        communicationButtonsData.buttonToHelp()
    }

    override fun buttonToSeeUserAgreement() {
        communicationButtonsData.buttonToSeeUserAgreement()
    }

}