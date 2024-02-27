package com.practicum.playlistmaker.search.ui

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.garbage__data.HistoryTrackClickListener
import com.practicum.playlistmaker.utils.AppPreferencesKeys
import com.practicum.playlistmaker.utils.SharedPreferencesMethods
import com.practicum.playlistmaker.garbage__domain.models.TracksList

class AdapterForHistoryTracks(
    private val context: Context,
    private val trackItemClickListener: HistoryTrackClickListener
) {
    private val adapterForHistoryTracks: AdapterForAPITracks =
        AdapterForAPITracks(context, mutableListOf(), trackItemClickListener)

    fun setRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = adapterForHistoryTracks
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    fun saveTrack(track: TracksList) {
        val trackList = SharedPreferencesMethods(context).getTrackListFromSP()
        trackList.removeAll { it.trackName == track.trackName && it.artistName == track.artistName }
        trackList.add(0, track)
        if (trackList.size > AppPreferencesKeys.HISTORY_TRACK_LIST_SIZE) {
            trackList.subList(AppPreferencesKeys.HISTORY_TRACK_LIST_SIZE, trackList.size).clear()
        }
        SharedPreferencesMethods(context).saveTrackListToSP(trackList)
        adapterForHistoryTracks.updateList(trackList.map { it.toTrackData() })
    }

    fun syncTracks() {
        val trackList = SharedPreferencesMethods(context).getTrackListFromSP()
        if (trackList.isNotEmpty()) {
            adapterForHistoryTracks.updateList(trackList.map { it.toTrackData() })
        }
    }

    fun checkIfHistoryListExists(): Boolean {
        return SharedPreferencesMethods(context).doesHistoryListExists()
    }

    fun clearHistoryList() {
        adapterForHistoryTracks.clearList()
        adapterForHistoryTracks.notifyDataSetChanged()
    }

    fun killHistoryList() {
        SharedPreferencesMethods(context).delFromSP(AppPreferencesKeys.KEY_HISTORY_LIST)
    }
}