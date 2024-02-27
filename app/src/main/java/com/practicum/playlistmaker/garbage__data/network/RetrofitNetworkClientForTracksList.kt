package com.practicum.playlistmaker.garbage__data.network

import com.practicum.playlistmaker.garbage__data.NetworkClientForTracksList
import com.practicum.playlistmaker.garbage__data.dto.ResponseForTracksList
import com.practicum.playlistmaker.garbage__data.dto.SearchRequestForTracksList
import com.practicum.playlistmaker.utils.AppPreferencesKeys
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClientForTracksList : NetworkClientForTracksList {

    private val itunesBaseUrl = AppPreferencesKeys.iTunesSearch

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ApiServiceForTracksList::class.java)

    override fun doRequest(dto: Any): ResponseForTracksList {
        if (dto is SearchRequestForTracksList) {
            val resp = itunesService.searchStep3iTunesAPI(dto.query).execute()

            val body = resp.body() ?: ResponseForTracksList()

            return body.apply { resultCode = resp.code() }
        } else {
            return ResponseForTracksList().apply { resultCode = 400 }
        }
    }
}