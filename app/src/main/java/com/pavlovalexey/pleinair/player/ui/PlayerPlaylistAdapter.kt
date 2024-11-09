package com.practicum.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.practicum.playlistmaker.databinding.UtilPlaylistViewInPlayerBinding
import com.practicum.playlistmaker.medialibrary.domain.model.Playlist
import com.practicum.playlistmaker.utils.setDebouncedClickListener

class PlayerPlaylistAdapter(private val clickListener: PlaylistClickListener) :
    RecyclerView.Adapter<PlayerPlaylistViewHolder>() {

    var playlist = ArrayList<Playlist>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerPlaylistViewHolder {
        val view = LayoutInflater
            .from(parent.context)

        return PlayerPlaylistViewHolder(UtilPlaylistViewInPlayerBinding.inflate(view, parent, false))
    }

    override fun getItemCount(): Int {
        return playlist.size
    }

    override fun onBindViewHolder(holder: PlayerPlaylistViewHolder, position: Int) {
        val playlist: Playlist = playlist[position]
        holder.bind(playlist)
        holder.itemView.setDebouncedClickListener() {
            clickListener.onPlaylistClick(playlist)
        }
    }

    interface PlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }
}