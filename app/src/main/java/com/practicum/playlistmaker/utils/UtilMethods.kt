package com.practicum.playlistmaker.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import java.text.SimpleDateFormat
import java.util.Locale

// internal методы для всяких разных функций

internal fun mmss(ms: Long?): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(ms)

internal fun openSettingsScreen (context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.data= Uri.fromParts("package", context.packageName, null)
    context.startActivity(intent)
}