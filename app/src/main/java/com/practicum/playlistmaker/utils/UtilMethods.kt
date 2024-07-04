package com.practicum.playlistmaker.utils

import android.util.Log
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

internal fun changeRussianWordsAsMinutes(countMinuteMillis: Int): String {
    Log.d("=== LOG ===", "=== changeRussianWordsAsMinutes ${countMinuteMillis}")
    val countMinute = countMinuteMillis / 60000
    val lastDigit = countMinute % 10
    val lastTwoDigits = countMinute % 100

    return when {
        lastTwoDigits in 11..19 -> "минут"
        lastDigit == 1 -> "минута"
        lastDigit in 2..4 -> "минуты"
        else -> "минут"
    }
}