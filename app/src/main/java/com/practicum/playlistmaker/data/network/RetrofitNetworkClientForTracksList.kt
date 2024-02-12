package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.NetworkClientForTracksList
import com.practicum.playlistmaker.data.dto.ResponseForTracksList
import com.practicum.playlistmaker.data.dto.SearchRequestForTracksList
import com.practicum.playlistmaker.data.preferences.AppPreferencesKeys
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