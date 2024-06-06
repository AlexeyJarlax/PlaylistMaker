package com.practicum.playlistmaker.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import java.text.SimpleDateFormat
import java.util.Locale
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// internal методы для всяких разных функций

internal fun mmss(ms: Long?): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(ms)

internal fun openSettingsScreen (context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.data= Uri.fromParts("package", context.packageName, null)
    context.startActivity(intent)
}

internal fun String.toIntList(): ArrayList<Int> {
    val type = object : TypeToken<ArrayList<Int>>() {}.type
    val result: ArrayList<Int> = Gson().fromJson(this, type)
    return result
}

internal fun Int.toTracksCount(): String {
    val preLastDigit = this % 100 / 10;
    if (preLastDigit == 1) return "$this треков"
    return when (this % 10) {
        1 -> "$this трек"
        2,3,4 -> "$this трека"
        else -> "$this треков"
    }
}