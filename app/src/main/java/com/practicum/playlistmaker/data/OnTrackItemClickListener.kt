package com.practicum.playlistmaker.data

//OnTrackItemClickListener - интерфейс для обработки истории

import com.practicum.playlistmaker.domain.models.Track

interface OnTrackItemClickListener {
    fun onTrackItemClick(track: Track)
}