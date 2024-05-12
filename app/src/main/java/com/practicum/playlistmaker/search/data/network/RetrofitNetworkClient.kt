package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.SearchRequest
import timber.log.Timber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val iTunesAPIService: ITunesAPIService) : NetworkClient {

    override suspend fun doRequest(request: SearchRequest): Response {
        return withContext(Dispatchers.IO) {
            try {
                val response = iTunesAPIService.findTrack(request.expression)
                Timber.d("=== class RetrofitNetworkClient  => fun doRequest(${request}))")
                response.apply { resultCode = 200
                    Timber.d("=== class RetrofitNetworkClient  => body.apply(${resultCode}))")
                }
            } catch (e: Throwable) {
                Response().apply { resultCode = 400
                    Timber.d("=== class RetrofitNetworkClient  => catch 400(${e.message}))")
                }
            }
        }
    }
}
