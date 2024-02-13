package com.practicum.playlistmaker.data

//OnTrackItemClickListener - интерфейс для обработки истории

import com.practicum.playlistmaker.domain.models.TracksList

interface HistoryTrackClickListener {
    fun onTrackItemClick(track: TracksList)
}