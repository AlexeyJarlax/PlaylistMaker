package com.practicum.playlistmaker.data.dto

// TrackResponse - класс данных, представляющий ответ от iTunes Search API.

class TrackSearchResponse(val results: List<ITunesTrack>) : Response()