package com.practicum.playlistmaker.medialibrary.favorites.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.favorites.domain.db.FavoritesInteractor
import kotlinx.coroutines.launch

class MLFavoritesViewModel(private val favoritesInteractor: FavoritesInteractor) : ViewModel() {

    private val _screenState = MutableLiveData<MLFavoritesScreenState>(MLFavoritesScreenState.Loading)
    val screenState: LiveData<MLFavoritesScreenState> = _screenState

    init {
        viewModelScope.launch {
            favoritesInteractor.getAllTracksSortedById().collect { trackList ->
                _screenState.value = MLFavoritesScreenState.Ready(trackList)
            }
        }
    }
}