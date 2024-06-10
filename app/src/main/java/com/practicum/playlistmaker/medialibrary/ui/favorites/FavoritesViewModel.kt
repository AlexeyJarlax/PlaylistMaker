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

    private val stateLiveData = MutableLiveData<FavoriteState>()
    fun observeState(): LiveData<FavoriteState> = stateLiveData

    init {
        viewModelScope.launch {
            favoritesTracksInteractor
                .getAllTracksSortedById()
                .collect { result ->
                    if (result.isEmpty()) {
                        stateLiveData.postValue(FavoriteState.Empty)
                    } else {
                        stateLiveData.postValue(FavoriteState.Content(result))
                    }
                }
        }
    }

    private fun fetchFavoriteTracks() {
        viewModelScope.launch {
            favoritesTracksInteractor.getAllTracksSortedById().collect { result ->
                if (result.isEmpty()) {
                    stateLiveData.postValue(FavoriteState.Empty)
                } else {
                    stateLiveData.postValue(FavoriteState.Content(result))
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