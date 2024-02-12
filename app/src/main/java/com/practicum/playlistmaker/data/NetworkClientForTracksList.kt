package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.ResponseForTracksList

interface NetworkClientForTracksList {
    fun doRequest(dto: Any): ResponseForTracksList
}