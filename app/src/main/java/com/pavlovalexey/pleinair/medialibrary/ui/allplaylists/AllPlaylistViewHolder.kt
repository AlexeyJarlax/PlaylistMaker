package com.practicum.playlistmaker.medialibrary.ui.allplaylists

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R

import com.practicum.playlistmaker.databinding.UtilPlaylistViewInMediatekaBinding
import com.practicum.playlistmaker.medialibrary.domain.model.Playlist
import com.practicum.playlistmaker.utils.GlideUrlLoader
import com.practicum.playlistmaker.utils.changeRussianWordsAsTracks

class AllPlaylistViewHolder(binding: UtilPlaylistViewInMediatekaBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val image: ImageView = binding.placeHolderPLVinM
    private val name: TextView = binding.playlistName
    private val tracksCount: TextView = binding.countTracks

    fun bind(model: Playlist) {
        name.text = model.playlistName
        val trackCount = "${model.tracksCount} ${changeRussianWordsAsTracks(model.tracksCount)}"
        tracksCount.text = trackCount
        GlideUrlLoader(R.drawable.ic_placeholder).loadImage(model.urlImage, image)
    }
}