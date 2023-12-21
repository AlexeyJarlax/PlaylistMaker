package com.practicum.playlistmaker.util

internal class MyCompObj { // глобальные переменные\константы

    companion object {
        // ключи и файлы в хранилище
        const val PREFS_NAME = "MyPrefs"
        const val PREFS_HISTORY_NAME = "SearchHistory"
        const val KEY_NIGHT_MODE = "nightMode"
        const val KEY_HISTORY_LIST = "key_for_history_list"
        // числовые константы
        const val SERVER_PROCESSING_MILLISECONDS : Long = 1500
    }
}
