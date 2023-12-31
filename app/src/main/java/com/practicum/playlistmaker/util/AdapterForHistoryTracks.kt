package com.practicum.playlistmaker.util

import android.content.Context
import android.content.SharedPreferences
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.OnTrackItemClickListener
import com.practicum.playlistmaker.AdapterForAPITracks
import com.practicum.playlistmaker.TrackData

class AdapterForHistoryTracks(
    private val context: Context,
    private val trackItemClickListener: OnTrackItemClickListener
) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(AppPreferencesKeys.PREFS_HISTORY_NAME, Context.MODE_PRIVATE)

    private val adapterForHistoryTracks: AdapterForAPITracks =
        AdapterForAPITracks(context, mutableListOf(), trackItemClickListener)

    fun setRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = adapterForHistoryTracks
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    fun saveTrack(
        trackName: String?,
        artistName: String?,
        trackTimeMillis: Long?,
        artworkUrl100: String?,
        collectionName: String?,
        releaseDate: String?,
        primaryGenreName: String?,
        country: String?
    ) {
        val trackList = getTrackListFromSharedPreferences()
        trackList.removeAll { it.trackName == trackName && it.artistName == artistName }
        trackList.add(
            0,
            Track(
                trackName.orEmpty(),
                artistName.orEmpty(),
                trackTimeMillis ?: 0L,
                artworkUrl100.orEmpty(),
                collectionName.orEmpty(),
                releaseDate.orEmpty(),
                primaryGenreName.orEmpty(),
                country.orEmpty()
            )
        )
        if (trackList.size > AppPreferencesKeys.HISTORY_TRACK_LIST_SIZE) {
            trackList.subList(AppPreferencesKeys.HISTORY_TRACK_LIST_SIZE, trackList.size).clear()
        }

        saveTrackListToSharedPreferences(trackList)
        adapterForHistoryTracks.updateList(trackList.map { it.toTrackData() })
    }

    fun syncTracks() {
        val trackList = getTrackListFromSharedPreferences()
        if (trackList.isNotEmpty()) {
            adapterForHistoryTracks.updateList(trackList.map { it.toTrackData() })
        }
    }

    private fun saveTrackListToSharedPreferences(trackList: List<Track>) {
        val jsonString = Gson().toJson(trackList)
        sharedPreferences.edit().putString(AppPreferencesKeys.KEY_HISTORY_LIST, jsonString).apply()
    }

    private fun getTrackListFromSharedPreferences(): MutableList<Track> {
        val jsonString = sharedPreferences.getString(AppPreferencesKeys.KEY_HISTORY_LIST, null)
        val type = object : TypeToken<List<Track>>() {}.type
        return Gson().fromJson(jsonString, type) ?: mutableListOf()
    }

    fun checkIfHistoryListExists(): Boolean {
        return sharedPreferences.contains(AppPreferencesKeys.KEY_HISTORY_LIST)
    }

    fun clearHistoryList() {
        adapterForHistoryTracks.clearList()
        adapterForHistoryTracks.notifyDataSetChanged()
    }

    fun killHistoryList() {
        val editor = sharedPreferences.edit()
        editor.remove(AppPreferencesKeys.KEY_HISTORY_LIST)
        editor.apply()
    }

    data class Track(
        val trackName: String?,
        val artistName: String?,
        val trackTimeMillis: Long?,
        val artworkUrl100: String?,
        val collectionName: String?,
        val releaseDate: String?,
        val primaryGenreName: String?,
        val country: String?
    ) {
        fun toTrackData() = TrackData(
            trackName,
            artistName,
            trackTimeMillis,
            artworkUrl100,
            collectionName,
            releaseDate,
            primaryGenreName,
            country
        )
    }
}