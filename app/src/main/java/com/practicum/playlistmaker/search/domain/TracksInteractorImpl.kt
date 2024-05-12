package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.TracksResponse
import com.practicum.playlistmaker.utils.AppPreferencesKeys
import kotlinx.coroutines.flow.Flow

class TracksInteractorImpl(private val history: TracksRepository) : TracksInteractor {

    override fun searchTracks(expression: String): Flow<TracksResponse> {
        return history.searchTracks(expression)
    }

    override fun saveToHistory(track: Track) {
        val array = loadFromHistory()
        if (array.contains(track)) {
            array.remove(track)
        }
        array.add(0, track)
        if (array.size > AppPreferencesKeys.HISTORY_TRACK_LIST_SIZE) {
            array.removeAt(AppPreferencesKeys.HISTORY_TRACK_LIST_SIZE)
        }
        history.saveToHistory(array)
    }

    override fun loadFromHistory(): ArrayList<Track> {
        return history.loadFromHistory()
    }

    override fun killHistory() {
        history.killHistory()
    }

    override fun getActiveList(): ArrayList<Track> {
        return loadFromHistory()
    }
}