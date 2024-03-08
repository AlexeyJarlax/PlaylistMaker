package com.practicum.playlistmaker.utils

import java.text.SimpleDateFormat
import java.util.Locale

// internal методы для всяких разных функций

internal fun mmss(ms: Long?): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(ms)