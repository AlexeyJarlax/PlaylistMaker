package com.practicum.playlistmaker.utils

internal object AppPreferencesKeys { // глобальные объекты

    // ключи и файлы в хранилище
    const val PREFS_NAME = "MyPrefs"
    const val KEY_NIGHT_MODE = "nightMode"
    const val KEY_HISTORY_LIST = "key_for_history_list"
    const val iTunesSearchUrl = "https://itunes.apple.com"

    // константы
    const val ALBUM_ROUNDED_CORNERS = 8
    const val HISTORY_TRACK_LIST_SIZE = 10
    const val CLICK_DEBOUNCE_DELAY = 500L // кулдаун для клика / милисекунда
    const val SEARCH_DEBOUNCE_DELAY = 2000L // отложенный поисковый запрос
    const val ONE_SECOND = 1000L // одна секунда
    const val PREFERENCES_TITLE = "PlayList_Maker_preferences"
    const val SEARCH_HISTORY = "search_history"
    const val AN_INSTANCE_OF_THE_TRACK_CLASS = "an instance of the Track class"
    const val INTERNET = "problems_with_internet"
    const val RESULTS = "problems_with_results"

}


