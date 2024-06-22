package com.practicum.playlistmaker.player.ui

import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.UtilPlaylistViewInPlayerBinding
import com.practicum.playlistmaker.medialibrary.domain.model.Playlist
import com.practicum.playlistmaker.utils.GlideUrlLoader
import com.practicum.playlistmaker.utils.changeRussianWordsAsTracks

class PlayerPlaylistViewHolder(binding: UtilPlaylistViewInPlayerBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val playlistCover = binding.placeHolderPLVinP
    private val playlistName = binding.playlistName
    private val tracksCount = binding.trackCount

    fun bind(model: Playlist) {
        playlistName.text = model.playlistName
        val trackCount = "${model.tracksCount} ${changeRussianWordsAsTracks(model.tracksCount)}"
        tracksCount.text = trackCount
        GlideUrlLoader(R.drawable.ic_placeholder).loadImage(model.urlImage, playlistCover)
        playlistCover.clipToOutline = true
    }
}