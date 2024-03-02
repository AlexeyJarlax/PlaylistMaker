package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.SearchResponseForTracksList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesAPIServiceForTracksList {
    @GET("/search?entity=song")
    fun findTrack(@Query("term") text: String): Call<SearchResponseForTracksList>
}