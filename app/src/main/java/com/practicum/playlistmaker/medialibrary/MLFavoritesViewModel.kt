package com.practicum.playlistmaker.medialibrary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MLFavoritesViewModel(private val plug: String) : ViewModel() {

    private val urlLiveData = MutableLiveData(plug)
    fun observeUrl(): LiveData<String> = urlLiveData
}