package com.practicum.playlistmaker.util

internal object AppPreferencesKeys { // глобальные объекты

    // ключи и файлы в хранилище
    const val PREFS_NAME = "MyPrefs"
    const val PREFS_HISTORY_NAME = "SearchHistory"
    const val KEY_NIGHT_MODE = "nightMode"
    const val KEY_HISTORY_LIST = "key_for_history_list"

    // числовые константы
    const val ALBUM_ROUNDED_CORNERS = 8
    const val HISTORY_TRACK_LIST_SIZE = 10
    const val CLICK_DEBOUNCE_DELAY = 500L // допустимое количество кликов / секунда
    const val SEARCH_DEBOUNCE_DELAY = 2000L // отложенный поисковый запрос
}


