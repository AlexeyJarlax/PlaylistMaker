package com.practicum.playlistmaker.util

import android.os.Handler
import android.os.Looper

class SecondsCounter(private val updateCallback: (Long) -> Unit) {

    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private var isRunning = false
    private var elapsedTime = 0L

    private val updateRunnable = object : Runnable {
        override fun run() {
            elapsedTime++
            updateCallback(elapsedTime)
            mainThreadHandler.postDelayed(this, 1000)
        }
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