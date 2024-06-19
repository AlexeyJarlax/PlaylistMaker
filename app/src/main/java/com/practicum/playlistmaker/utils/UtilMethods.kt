package com.practicum.playlistmaker.utils

import java.text.SimpleDateFormat
import java.util.Locale

// internal методы для всяких разных функций

internal fun msToSs(ms: Long?): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(ms)

internal fun msToMm(timeInMilliseconds: String): String {
    return SimpleDateFormat("mm", Locale.getDefault()).format(timeInMilliseconds.toInt())
}

internal fun changeRussianWordsAsTracks(countTrack: Int): String {
    val num = countTrack % 100
    return when {
        num in 10..20 -> "треков"
        num % 10 == 1 -> "трек"
        num % 10 in 2..4 -> "трека"
        else -> "треков"
    }
}

fun changeRussianWordsAsMinutes(countMinute: Int): String {
    val num = countMinute % 100
    return when {
        num in 11..19 -> "минут"
        num % 10 == 1 -> "минута"
        num % 10 in 2..4 -> "минуты"
        else -> "минут"
    }
}