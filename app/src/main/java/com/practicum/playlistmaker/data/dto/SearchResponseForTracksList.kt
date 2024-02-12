package com.practicum.playlistmaker.data.dto

// TrackResponse - класс данных, представляющий ответ от iTunes Search API.

class SearchResponseForTracksList(val results: List<ITunesForTracksList>) : ResponseForTracksList()