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

    private val _stateLiveData = MutableLiveData<FavoritesState>()
    val stateLiveData: LiveData<FavoritesState> = _stateLiveData

    init {
        viewModelScope.launch {
            favoritesTracksInteractor
                .getAllTracksSortedById()
                .collect { result ->
                    if (result.isEmpty()) {
                        _stateLiveData.value = FavoritesState.Error
                    } else {
                        _stateLiveData.value = FavoritesState.Ready(result)
                    }
                }
        }
    }

    private fun fetchFavoriteTracks() {
        viewModelScope.launch {
            favoritesTracksInteractor.getAllTracksSortedById().collect { result ->
                if (result.isEmpty()) {
                    _stateLiveData.postValue(FavoritesState.Error)
                } else {
                    _stateLiveData.postValue(FavoritesState.Ready(result))
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