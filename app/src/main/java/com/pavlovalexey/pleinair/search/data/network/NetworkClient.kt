package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.SearchRequest

interface NetworkClient {
    suspend fun doRequest(request: SearchRequest): Response
}