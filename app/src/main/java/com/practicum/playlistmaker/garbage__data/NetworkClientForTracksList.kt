package com.practicum.playlistmaker.garbage__data

import com.practicum.playlistmaker.garbage__data.dto.ResponseForTracksList

interface NetworkClientForTracksList {
    fun doRequest(dto: Any): ResponseForTracksList
}