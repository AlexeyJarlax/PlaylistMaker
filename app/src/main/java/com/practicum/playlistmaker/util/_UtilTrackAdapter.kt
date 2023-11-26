package com.practicum.playlistmaker.util

// адаптор для RecyclerView, Glide

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R

class TrackItem(val track: Track) {}

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackNameTextView: TextView = itemView.findViewById(R.id.track_name_text_view)
    private val artistNameTextView: TextView = itemView.findViewById(R.id.artist_name_text_view)
    private val trackTimeTextView: TextView = itemView.findViewById(R.id.track_time_text_view)
    private val artworkImageView: ImageView = itemView.findViewById(R.id.artwork_image_view)
    private val rightArrowButton: ImageView = itemView.findViewById(R.id.button_right_arrow)

    fun bind(trackItem: TrackItem, onItemClick: (String) -> Unit) {
        val track = trackItem.track
        trackNameTextView.text = track.trackName
        artistNameTextView.text = track.artistName
        trackTimeTextView.text = track.trackTime

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .fitCenter()
            .transform(RoundedCorners(8)) // закругление уголов у пикчи
            .into(artworkImageView)

        rightArrowButton.setOnClickListener {  // стрелка > запускает переход на поиск песни в ЯндексМузыке
            onItemClick(track.webUrl)
        }
    }
}

class TrackAdapter(
    private val context: Context,
    private val trackList: List<Track>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout._util_track_item_layout, parent, false)
        return TrackViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = trackList[position]
        val trackItem = TrackItem(track)
        holder.bind(trackItem) { webUrl ->
            // Обработка клика по кнопке справа
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webUrl))
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}

