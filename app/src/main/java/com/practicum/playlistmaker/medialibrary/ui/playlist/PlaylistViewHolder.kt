package com.practicum.playlistmaker.medialibrary.ui.playlist

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.practicum.playlistmaker.databinding.UtilPlaylistViewInMediatekaBinding
import com.practicum.playlistmaker.medialibrary.domain.model.Playlist
import com.practicum.playlistmaker.utils.ArtworkUrlLoader
import com.practicum.playlistmaker.utils.changeRussianWords

class PlaylistViewHolder(binding: UtilPlaylistViewInMediatekaBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val image: ImageView = binding.placeHolderPLVinM
    private val name: TextView = binding.playlistName
    private val tracksCount: TextView = binding.countTracks
    private val artworkUrlLoader = ArtworkUrlLoader()

    fun bind(model: Playlist) {
        name.text = model.playlistName
        val trackCount = "${model.tracksCount} ${changeRussianWords(model.tracksCount)}"
        tracksCount.text = trackCount
        artworkUrlLoader.loadImage(model.urlImage, image)
    }
}