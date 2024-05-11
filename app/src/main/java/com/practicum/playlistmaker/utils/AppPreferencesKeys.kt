package com.practicum.playlistmaker.utils

internal object AppPreferencesKeys { // глобальные объекты

    // ключи и файлы в хранилище
    const val PREFS_NAME = "MyPrefs"
    const val KEY_NIGHT_MODE = "nightMode"
    const val SEARCH_HISTORY = "search_history"
    const val AN_INSTANCE_OF_THE_TRACK_CLASS = "an instance of the Track class"

    // константы
    const val ALBUM_ROUNDED_CORNERS = 8
    const val HISTORY_TRACK_LIST_SIZE = 10
    const val CLICK_DEBOUNCE_DELAY = 1500L // кулдаун для клика / милисекунда
    const val TWO_SECONDS = 2000L // две секунды  / отложенный поисковый запрос
    const val ONE_SECOND = 1000L // одна секунда
    const val iTunesSearchUrl = "https://itunes.apple.com"

    // заглушки
    const val INTERNET_EMPTY = "problems_with_internet"
    const val RESULTS_EMPTY = "no_results"
    const val FAVORITES_EMPTY = "favorites_empty"
    const val PLAYLISTS_EMPTY = "playlists_empty"
    const val LOADING = "loading"
}


