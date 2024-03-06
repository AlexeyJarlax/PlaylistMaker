package com.practicum.playlistmaker.search.data.dto

// TrackResponse - класс данных, представляющий ответ от iTunes Search API.

class SearchResponseForTracksList(val results: ArrayList<TrackDTO>) : ResponseForTracksList()