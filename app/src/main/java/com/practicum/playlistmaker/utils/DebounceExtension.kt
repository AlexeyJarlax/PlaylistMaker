package com.practicum.playlistmaker.utils

import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//********************************** дебаунсер для поиска во время ввода и защиты от лишних кликов на Корутине
class DebounceExtension(private val delayMillis: Long, private val action: () -> Unit) {
    private var debounceJob: Job? = null
    fun debounce() {
        debounceJob?.cancel()
        debounceJob = CoroutineScope(Dispatchers.Main).launch {
            delay(delayMillis)
            action.invoke()
        }
    }
}

// расширение для улучшенного кликера на Корутине
fun View.setDebouncedClickListener(delayMillis: Long = AppPreferencesKeys.HALF_SECOND_DELAY, onClick: () -> Unit) {
    var debounceJob: Job? = null
    setOnClickListener {
        debounceJob?.cancel()
        debounceJob = CoroutineScope(Dispatchers.Main).launch {
            delay(delayMillis)
            onClick()
        }
    }
}
