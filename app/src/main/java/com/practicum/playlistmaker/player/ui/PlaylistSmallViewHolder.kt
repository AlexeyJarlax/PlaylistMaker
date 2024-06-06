package com.practicum.playlistmaker.player.ui

import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistSmallItemBinding
import com.practicum.playlistmaker.medialibrary.domain.db.Playlist
import com.practicum.playlistmaker.utils.toTracksCount

class PlaylistSmallViewHolder(private val binding: PlaylistSmallItemBinding) :
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