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
        context.getSharedPreferences(MyCompObj.PREFS_HISTORY_NAME, Context.MODE_PRIVATE)
    private val adapterForHistoryTracks: AdapterForAPITracks // Создаем History экземпляр адаптера

    init {
        adapterForHistoryTracks = AdapterForAPITracks(context, mutableListOf(), trackItemClickListener)
    }

    fun setRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = adapterForHistoryTracks
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    fun saveTrack(trackName: String, artistName: String, trackTimeMillis: Long, artworkUrl100: String) {
        val trackList = getTrackListFromSharedPreferences()
        trackList.removeAll { it.trackName == trackName && it.artistName == artistName }
        trackList.add(0, Track(trackName, artistName, trackTimeMillis, artworkUrl100))
        if (trackList.size > 10) {
            trackList.subList(10, trackList.size).clear()
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
        sharedPreferences.edit().putString(MyCompObj.KEY_HISTORY_LIST, jsonString).apply()
    }

    private fun getTrackListFromSharedPreferences(): MutableList<Track> {
        val jsonString = sharedPreferences.getString(MyCompObj.KEY_HISTORY_LIST, null)
        val type = object : TypeToken<List<Track>>() {}.type
        return Gson().fromJson(jsonString, type) ?: mutableListOf()
    }

    fun clearHistoryList() {
        adapterForHistoryTracks.clearList()
        adapterForHistoryTracks.notifyDataSetChanged()
    }

    fun killHistoryList() {
        val editor = sharedPreferences.edit()
        editor.remove(MyCompObj.KEY_HISTORY_LIST)
        editor.apply()
    }

    data class Track(
        val trackName: String,
        val artistName: String,
        val trackTimeMillis: Long,
        val artworkUrl100: String
    ) {
        fun toTrackData() = TrackData(trackName, artistName, trackTimeMillis, artworkUrl100)
    }
}