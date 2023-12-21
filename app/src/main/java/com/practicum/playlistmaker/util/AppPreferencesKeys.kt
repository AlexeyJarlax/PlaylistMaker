package com.practicum.playlistmaker.util

internal class AppPreferencesKeys { // глобальные переменные\константы

    companion object {
        // ключи и файлы в хранилище
        const val PREFS_NAME = "MyPrefs"
        const val PREFS_HISTORY_NAME = "SearchHistory"
        const val KEY_NIGHT_MODE = "nightMode"
        const val KEY_HISTORY_LIST = "key_for_history_list"
        // числовые константы
        const val ALBUM_ROUNDED_CORNERS = 8
        const val SERVER_PROCESSING_MILLISECONDS : Long = 1500
        const val HISTORY_TRACK_LIST_SIZE = 10
    }
}
