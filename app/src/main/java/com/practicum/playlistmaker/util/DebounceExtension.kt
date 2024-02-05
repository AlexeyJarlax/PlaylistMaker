package com.practicum.playlistmaker.util

import android.os.Handler
import android.os.Looper
import android.view.View

//****************************************** дебаунсер для поиска во время ввода и защиты от лишних кликов
class DebounceExtension(private val delayMillis: Long, private val action: () -> Unit) {
    private val handler = Handler(Looper.getMainLooper())

    fun debounce() {
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({
            action.invoke()
        }, delayMillis)
    }
}

// расширение для улучшенного кликера
fun View.setDebouncedClickListener(delayMillis: Long = AppPreferencesKeys.CLICK_DEBOUNCE_DELAY, onClick: () -> Unit) {
    val debouncer = DebounceExtension(delayMillis, onClick)
    setOnClickListener {
        debouncer.debounce()
    }
}