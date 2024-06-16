package com.practicum.playlistmaker.medialibrary.ui.playlist

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.practicum.playlistmaker.databinding.UtilPlaylistViewInMediatekaBinding
import com.practicum.playlistmaker.medialibrary.domain.model.Playlist
import com.practicum.playlistmaker.utils.setDebouncedClickListener

class PlaylistAdapter() : RecyclerView.Adapter<PlaylistViewHolder>() {

    var playlist = ArrayList<Playlist>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater
            .from(parent.context)

        return PlaylistViewHolder(UtilPlaylistViewInMediatekaBinding.inflate(view, parent, false))
    }

    override fun getItemCount(): Int {
        return playlist.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist: Playlist = playlist[position]
        holder.bind(playlist)
        holder.itemView.setDebouncedClickListener() {
            Log.d("=== LOG ===", "===  class PlaylistAdapter => holder.itemView.setOnClickListener ${holder.itemView}")
        }
    }
}