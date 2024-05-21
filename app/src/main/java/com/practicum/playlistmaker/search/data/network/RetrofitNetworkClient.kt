package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.SearchRequest
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val iTunesAPIService: ITunesAPIService) : NetworkClient {

    override suspend fun doRequest(request: SearchRequest): Response {
        return withContext(Dispatchers.IO) {
            try {
                val response = iTunesAPIService.findTrack(request.expression)
                Log.d("=== LOG ===", "===  class RetrofitNetworkClient  => fun doRequest(${request}))")
                response.apply { resultCode = 200
                    Log.d("=== LOG ===", "===  class RetrofitNetworkClient  => body.apply(${resultCode}))")
                }
            } catch (e: Throwable) {
                Response().apply { resultCode = 400
                    Log.d("=== LOG ===", "===  class RetrofitNetworkClient  => catch 400(${e.message}))")
                }
            }
        }
    }
}
