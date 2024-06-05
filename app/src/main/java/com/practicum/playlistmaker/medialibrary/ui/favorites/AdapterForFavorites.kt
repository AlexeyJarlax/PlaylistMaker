package com.practicum.playlistmaker.medialibrary.ui.favorites

import com.practicum.playlistmaker.search.ui.TrackViewHolder
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.practicum.playlistmaker.databinding.UtilItemTrackBinding
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.setDebouncedClickListener

class AdapterForFavorites(val onClick: (Track) -> Unit) : RecyclerView.Adapter<TrackViewHolder>() {

    var favoritTracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(UtilItemTrackBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int = favoritTracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(favoritTracks[position])
        holder.itemView.setDebouncedClickListener {
            val clickedTrack = favoritTracks[position]
            onClick(clickedTrack)
        }
    }
}