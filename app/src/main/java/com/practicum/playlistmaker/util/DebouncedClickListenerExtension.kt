package com.practicum.playlistmaker.util

import android.view.View


class Debouncer(private val debounceInterval: Long = AppPreferencesKeys.CLICK_DEBOUNCE_DELAY) {
    private var lastClickTime: Long = 0

    fun clickDebounce(): Boolean {
        val currentTime = System.currentTimeMillis()
        return if (currentTime - lastClickTime > debounceInterval) {
            lastClickTime = currentTime
            true
        } else {
            false
        }
    }
}

fun View.setDebouncedClickListener(onClick: () -> Unit) {
    val debouncer = Debouncer()
    setOnClickListener {
        if (debouncer.clickDebounce()) {
            onClick.invoke()
        }
    }
}