package com.practicum.playlistmaker.medialibrary.ui.allplaylists

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.practicum.playlistmaker.databinding.UtilPlaylistViewInMediatekaBinding
import com.practicum.playlistmaker.medialibrary.domain.model.Playlist
import com.practicum.playlistmaker.utils.setDebouncedClickListener

class AllPlaylistsAdapter(private val clickListener: PlaylistClickListener) :
    RecyclerView.Adapter<AllPlaylistViewHolder>() {

    var playlist = ArrayList<Playlist>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllPlaylistViewHolder {
        val view = LayoutInflater
            .from(parent.context)

        return AllPlaylistViewHolder(UtilPlaylistViewInMediatekaBinding.inflate(view, parent, false))
    }

    override fun getItemCount(): Int {
        return playlist.size
    }

    override fun onBindViewHolder(holder: AllPlaylistViewHolder, position: Int) {
        val playlist: Playlist = playlist[position]
        holder.bind(playlist)
        holder.itemView.setDebouncedClickListener() {
            Log.d("=== LOG ===", "===  class PlaylistAdapter => holder.itemView.setOnClickListener ${playlist.id}")
            clickListener.onPlaylistClick(playlist.id)
        }
    }

    interface PlaylistClickListener {
        fun onPlaylistClick(playlistId: Int)
    }
}