package com.practicum.playlistmaker.data.network

// NetworkAndApiService - интерфейс и объект для iTunes Search API, метод для поиска песен

import android.app.Activity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.AppPreferencesKeys
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.solvingAbsentProblem
import com.practicum.playlistmaker.presentation.solvingConnectionProblem
import com.practicum.playlistmaker.presentation.toast
import com.practicum.playlistmaker.ui.TrackResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object NetworkService {
    private val retrofit = Retrofit.Builder()
        .baseUrl(AppPreferencesKeys.iTunesSearch)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val iTunesApiService: ApiService = retrofit.create(ApiService::class.java)
}

interface ApiService {
    @GET("search")
    fun searchStep3iTunesAPI(
        @Query("term") query: String
    ): Call<TrackResponse>
}

fun searchStep3iTunesAPI(
    query: String,
    activity: Activity,
    callback: (List<Track>) -> Unit
) {
    Timber.d("Запускаем метод searchStep3iTunesAPI с параметрами Query: $query и Callback")
    NetworkService.iTunesApiService.searchStep3iTunesAPI(query).enqueue(object : Callback<TrackResponse> {
        override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
            if (response.isSuccessful) {
                    val trackResponse = response.body()
                    val trackData = if (trackResponse?.results?.isNotEmpty() == true) {
                        trackResponse.results.map { track ->
                            val releaseDateTime = try {
                                track.releaseDate?.let {
                                    LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME)
                                } ?: LocalDateTime.MIN
                            } catch (e: DateTimeParseException) {
                                Timber.e(e, "Ошибка при разборе даты: ${track.releaseDate}")
                                LocalDateTime.MIN
                            }
                            Track(
                                track.trackName ?: "",
                                track.artistName ?: "",
                                track.trackTimeMillis ?: 0,
                                track.artworkUrl100 ?: "",
                                track.collectionName ?: "",
                                releaseDateTime.year.toString(),
                                track.primaryGenreName ?: "",
                                track.country ?: "",
                                track.previewUrl ?: ""
                            )
                       }
                    } else {
                        Timber.d("Метод performSearch => response.isSuccessful! => emptyList() таких песен нет")
                        activity.solvingAbsentProblem() // вызываем заглушку о пустом листе запроса
                        emptyList()
                    }
                    callback(trackData)
                    Timber.d("Метод performSearch => response.isSuccessful! => callback(trackData): $trackData")
                } else {
                    val error = when (response.code()) {
                        400 -> activity.getString(R.string.error400)
                        401 -> activity.getString(R.string.error401)
                        403 -> activity.getString(R.string.error403)
                        404 -> activity.getString(R.string.error404)
                        500 -> activity.getString(R.string.error500)
                        503 -> activity.getString(R.string.error503)
                        else -> activity.getString(R.string.error0)
                    }
                    Timber.d(error)
                    activity.toast(error)
                    onFailure(call, Throwable(error))
                }
            }

        override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
            activity.solvingConnectionProblem(query, callback) // вызываем заглушку об ошибке соединения
            val trackData = emptyList<Track>()
            callback(trackData)
            }
        })
    }