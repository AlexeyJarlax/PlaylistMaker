package com.practicum.playlistmaker.search.data

import android.content.SharedPreferences
import com.google.gson.Gson
import timber.log.Timber

import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.AppPreferencesKeys
import com.practicum.playlistmaker.search.data.dto.SearchRequest
import com.practicum.playlistmaker.search.data.dto.SearchResponse
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.domain.models.TracksResponse

class TracksRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val networkClient: NetworkClient,
    private val gson: Gson
) :
    TracksRepository {

    override fun saveToHistory(history: ArrayList<Track>) {
        val json = gson.toJson(history)
        sharedPreferences.edit().putString(AppPreferencesKeys.SEARCH_HISTORY, json).apply()
    }

    override fun loadFromHistory(): ArrayList<Track> {
        val json = sharedPreferences.getString(AppPreferencesKeys.SEARCH_HISTORY, null) ?: return arrayListOf()
        return ArrayList(gson.fromJson(json, Array<Track>::class.java).toList())
    }

    override fun killHistory() {
        sharedPreferences.edit().remove(AppPreferencesKeys.SEARCH_HISTORY).apply()
    }

    override fun searchTracks(expression: String): TracksResponse {
        val response = networkClient.doRequest(SearchRequest(expression))
        if (response.resultCode == 200) {
            val trackList = (response as SearchResponse).results.map {
                Track(
                    trackId = it.trackId,
                    trackName = it.trackName ?: "",
                    artistName = it.artistName ?: "",
                    trackTimeMillis = it.trackTimeMillis ?: 0L,
                    artworkUrl100 = it.artworkUrl100 ?: "",
                    collectionName = it.collectionName ?: "",
                    releaseDate = it.releaseDate ?: "",
                    primaryGenreName = it.primaryGenreName ?: "",
                    country = it.country ?: "",
                    previewUrl = it.previewUrl ?: ""
                )
            }
            Timber.d("=== class TracksRepositoryImpl => return TracksResponse(${trackList})")
            return TracksResponse(trackList, response.resultCode)
        } else return TracksResponse(emptyList(), response.resultCode)
    }
}