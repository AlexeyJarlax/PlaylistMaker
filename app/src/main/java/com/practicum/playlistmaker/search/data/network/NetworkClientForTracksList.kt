package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.ResponseForTracksList
import com.practicum.playlistmaker.search.data.dto.SearchRequestForTracksList

interface NetworkClientForTracksList {
    fun doRequest(request: SearchRequestForTracksList): ResponseForTracksList
}