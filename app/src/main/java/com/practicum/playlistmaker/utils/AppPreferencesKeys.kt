package com.practicum.playlistmaker.utils

internal object AppPreferencesKeys { // глобальные объекты

    // ключи и файлы в хранилище
    const val PREFS_NAME = "MyPrefs"
    const val KEY_NIGHT_MODE = "nightMode"
    const val SEARCH_HISTORY = "search_history"
    const val AN_INSTANCE_OF_THE_TRACK_CLASS = "an instance of the Track class"
    const val PLAYLIST_KEY = "an instance of the playlist"
    const val iTunesSearchUrl = "https://itunes.apple.com"

    // числовые константы
    const val ALBUM_ROUNDED_CORNERS = 8
    const val HISTORY_TRACK_LIST_SIZE = 6
    const val HALF_SECOND_DELAY = 500L // кулдаун для клика / милисекунда
    const val TWO_SECONDS = 2000L // две секунды  / отложенный поисковый запрос
    const val ONE_SECOND = 1000L // одна секунда
    const val THREE_HUNDRED_MILLISECONDS = 300L // период обновления для музыкального плеера

    // заглушки
    const val INTERNET_EMPTY = "problems_with_internet"
    const val RESULTS_EMPTY = "no_results"
    const val FAVORITES_EMPTY = "favorites_empty"
    const val PLAYLISTS_EMPTY = "playlists_empty"
    const val LOADING = "loading"
    const val HIDE = "hide"
}


