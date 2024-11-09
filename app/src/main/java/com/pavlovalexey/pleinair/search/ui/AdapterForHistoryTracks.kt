package com.practicum.playlistmaker.search.ui

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.practicum.playlistmaker.databinding.UtilItemTrackBinding
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.setDebouncedClickListener

class AdapterForHistoryTracks(val onClick: (Track) -> Unit) : RecyclerView.Adapter<TrackViewHolder>() {

    var searchHistoryTracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(UtilItemTrackBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int = searchHistoryTracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(searchHistoryTracks[position])
        holder.itemView.setDebouncedClickListener {
            val clickedTrack = searchHistoryTracks[position]
            onClick(clickedTrack)
        }
    }
}