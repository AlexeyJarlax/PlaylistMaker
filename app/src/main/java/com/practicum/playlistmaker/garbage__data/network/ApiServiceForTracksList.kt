package com.practicum.playlistmaker.garbage__data.network

import com.practicum.playlistmaker.garbage__data.dto.SearchResponseForTracksList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceForTracksList {
    @GET("search")
    fun searchStep3iTunesAPI(
        @Query("term") query: String
    ): Call<SearchResponseForTracksList>
}