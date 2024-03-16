package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.SearchRequest
import timber.log.Timber

class RetrofitNetworkClient(private val iTunesAPIService: ITunesAPIService) : NetworkClient {

    override fun doRequest(request: SearchRequest): Response {
        return try {
            val resp = iTunesAPIService.findTrack(request.expression).execute()
            val body = resp.body() ?: Response()
            Timber.d("=== class RetrofitNetworkClient  => fun doRequest(${request}))")
            body.apply {
                resultCode = resp.code()
                Timber.d("=== class RetrofitNetworkClient  => body.apply(${resultCode}))")
            }
        } catch (exception: Exception) {
            Response().apply {
                resultCode = 400
                Timber.d("=== class RetrofitNetworkClient  => catch 400(${exception}))")
            }

        }
    }

}