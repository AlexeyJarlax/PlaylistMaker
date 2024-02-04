package com.practicum.playlistmaker.util

import android.content.Context
import android.os.Handler
import android.os.Looper


internal class Debouncer(private val context: Context)  {

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, AppPreferencesKeys.CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

}