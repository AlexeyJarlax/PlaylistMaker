package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.ResponseForTracksList
import com.practicum.playlistmaker.search.data.dto.SearchRequestForTracksList
import com.practicum.playlistmaker.utils.AppPreferencesKeys
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class RetrofitNetworkClientForTracksList : NetworkClientForTracksList {

    private val retrofit = Retrofit.Builder()
        .baseUrl(AppPreferencesKeys.iTunesSearchUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesAPIServiceForTracksList::class.java)

    override fun doRequest(request: SearchRequestForTracksList): ResponseForTracksList {
        return try {
            val resp = iTunesService.findTrack(request.expression).execute()
            val body = resp.body() ?: ResponseForTracksList()
            Timber.d("=== class RetrofitNetworkClient  => fun doRequest(${request}))")
            body.apply {
                resultCode = resp.code()
                Timber.d("=== class RetrofitNetworkClient  => body.apply(${resultCode}))")
            }
        } catch (exception: Exception) {
            ResponseForTracksList().apply {
                resultCode = 400
                Timber.d("=== class RetrofitNetworkClient  => catch 400(${exception}))")
            }

        }
    }

}