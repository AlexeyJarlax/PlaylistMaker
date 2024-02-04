package com.practicum.playlistmaker.util

import android.view.View

class Debouncer() {
    private var lastClickTime: Long = 0
    private var lastInputTime: Long = 0

    fun clickDebounce(): Boolean {
        val currentTime = System.currentTimeMillis()
        return if (currentTime - lastClickTime > AppPreferencesKeys.CLICK_DEBOUNCE_DELAY) {
            lastClickTime = currentTime
            true
        } else {
            false
        }
    }

//    fun inputDebounce(): Boolean {
//        val currentTime = System.currentTimeMillis()
//        return if (currentTime - lastInputTime > 10000) {
//            lastInputTime = currentTime
//            true
//        } else {
//            false
//        }
//    }

}

fun View.setDebouncedClickListener(onClick: () -> Unit) {
    val debouncer = Debouncer()
    setOnClickListener {
        if (debouncer.clickDebounce()) {
            onClick.invoke()
        }
    }
}

