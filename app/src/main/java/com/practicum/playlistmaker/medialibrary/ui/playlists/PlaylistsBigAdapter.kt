package com.practicum.playlistmaker.medialibrary.ui.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.PlaylistBigItemBinding
import com.practicum.playlistmaker.medialibrary.domain.db.Playlist

class PlaylistsBigAdapter(val onClick: (Playlist) -> Unit) :
    RecyclerView.Adapter<PlaylistBigViewHolder>() {

    var playlists = ArrayList<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistBigViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistBigViewHolder(PlaylistBigItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: PlaylistBigViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { onClick(playlists[position]) }
    }

}