package com.practicum.playlistmaker.search.data.dto

// TrackResponse - класс данных, представляющий ответ от iTunes Search API.

class SearchResponse(val results: ArrayList<TrackDTO>) : Response()