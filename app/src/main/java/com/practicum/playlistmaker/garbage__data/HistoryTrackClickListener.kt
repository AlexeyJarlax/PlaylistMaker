package com.practicum.playlistmaker.garbage__data

//OnTrackItemClickListener - интерфейс для обработки истории

import com.practicum.playlistmaker.garbage__domain.models.TracksList

interface HistoryTrackClickListener {
    fun onTrackItemClick(track: TracksList)
}