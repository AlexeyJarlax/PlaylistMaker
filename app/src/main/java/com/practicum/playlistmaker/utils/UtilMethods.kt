package com.practicum.playlistmaker.utils

import java.text.SimpleDateFormat
import java.util.Locale

// internal методы для всяких разных функций

internal fun mmss(ms: Long?): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(ms)

internal fun changeRussianWords(countTrack: Int): String {
    val num = countTrack % 100
    return when {
        num in 10..20 -> "треков"
        num % 10 == 1 -> "трек"
        num % 10 in 2..4 -> "трека"
        else -> "треков"
    }
}