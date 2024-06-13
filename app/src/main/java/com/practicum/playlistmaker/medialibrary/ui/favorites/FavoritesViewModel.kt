package com.practicum.playlistmaker.medialibrary.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.others.FavoritesTracksInteractor
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoritesTracksInteractor: FavoritesTracksInteractor,
    private val trackInteractor: TracksInteractor
) :
    ViewModel() {

    private val _stateLiveData = MutableLiveData<FavoriteState>()
    val stateLiveData: LiveData<FavoriteState> = _stateLiveData

    init {
        viewModelScope.launch {
            favoritesTracksInteractor
                .getAllTracksSortedById()
                .collect { result ->
                    if (result.isEmpty()) {
                        _stateLiveData.value = FavoriteState.Error
                    } else {
                        _stateLiveData.value = FavoriteState.Ready(result)
                    }
                }
        }
    }

    private fun fetchFavoriteTracks() {
        viewModelScope.launch {
            favoritesTracksInteractor.getAllTracksSortedById().collect { result ->
                if (result.isEmpty()) {
                    _stateLiveData.postValue(FavoriteState.Error)
                } else {
                    _stateLiveData.postValue(FavoriteState.Ready(result))
                }
            }
        }
    }

    fun addTrackToHistory(track: Track) {
        trackInteractor.saveToHistory(track)
        addTrackToFavorites(track)
    }

    fun addTrackToFavorites(track: Track) {
        viewModelScope.launch {
            favoritesTracksInteractor.addTrack(track)
            fetchFavoriteTracks()
        }
    }
}