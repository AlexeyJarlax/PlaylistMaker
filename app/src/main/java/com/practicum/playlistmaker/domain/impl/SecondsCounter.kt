package com.practicum.playlistmaker.domain.impl

import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.data.preferences.AppPreferencesKeys

//****************************************** секундный счетчик на обработку времени проигрывания
class SecondsCounter(private val updateCallback: (Long) -> Unit) {

    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private var isRunning = false
    private var elapsedTime = 0L

    val updateRunnable = object : Runnable {
        override fun run() {
            elapsedTime++
            updateCallback(elapsedTime)
            mainThreadHandler.postDelayed(this, AppPreferencesKeys.ONE_SECOND)
        }
    }

    fun handlerRepeater(onThreadComplete: () -> Unit) { // повторятель
        val handlerRepeater = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                handlerRepeater.post {
                    onThreadComplete()
                }
                handlerRepeater.postDelayed(this, AppPreferencesKeys.ONE_SECOND)
            }
        }
        handlerRepeater.post(runnable)
    }

    fun start() {
        if (!isRunning) {
            isRunning = true
            mainThreadHandler.post(updateRunnable)
        }
    }

    fun stop() {
        if (isRunning) {
            isRunning = false
            mainThreadHandler.removeCallbacks(updateRunnable)
        }
    }

    fun reset() {
        elapsedTime = 0L
        updateCallback(elapsedTime)
    }
}