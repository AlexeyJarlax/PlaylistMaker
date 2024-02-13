package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.dto.SearchResponseForTracksList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceForTracksList {
    @GET("search")
    fun searchStep3iTunesAPI(
        @Query("term") query: String
    ): Call<SearchResponseForTracksList>
}