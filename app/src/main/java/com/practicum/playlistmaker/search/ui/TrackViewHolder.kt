package com.practicum.playlistmaker.search.ui

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.UtilItemTrackBinding
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.GlideUrlLoader

class TrackViewHolder(private val binding: UtilItemTrackBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val artworkImageView: ImageView = itemView.findViewById(R.id.artwork_image_view)

    fun bind(track: Track) {
        binding.trackNameTextView.text = track.trackName
        binding.artistNameTextView.text = track.artistName
        binding.trackDurationTextView.text = track.trackTime
        track.artworkUrl100.let {GlideUrlLoader(R.drawable.ic_placeholder).loadImage(it, artworkImageView)}
    }
}