package com.practicum.playlistmaker.search.domain

import androidx.core.util.Consumer
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.TracksResponse
import com.practicum.playlistmaker.utils.AppPreferencesKeys

class TracksInteractorImpl(private val history: TracksRepository) : TracksInteractor {

    override fun searchTracks(expression: String, consumer: Consumer<TracksResponse>) {
        val thread = Thread { consumer.accept(history.searchTracks(expression)) }
        thread.start()
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