package com.practicum.playlistmaker.medialibrary.ui.playlists

import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistBigItemBinding
import com.practicum.playlistmaker.medialibrary.domain.db.Playlist
import com.practicum.playlistmaker.utils.toTracksCount

class PlaylistBigViewHolder(private val binding: PlaylistBigItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist: Playlist) {
        with(binding) {
            tvTitle.text = playlist.title
            tvCount.text = playlist.count.toTracksCount()
            if (playlist.poster==null) { ivPoster.setImageResource(R.drawable.ic_placeholder) }
            else { ivPoster.setImageURI(playlist.poster) }
        }
    }

}