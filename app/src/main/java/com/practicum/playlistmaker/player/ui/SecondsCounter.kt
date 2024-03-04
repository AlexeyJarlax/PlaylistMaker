package com.practicum.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.databinding.ActivityPlayBinding
import com.practicum.playlistmaker.utils.AppPreferencesKeys
import java.util.Locale
import java.util.concurrent.TimeUnit

//****************************************** секундный счетчик на обработку времени проигрывания
class SecondsCounter(private val updateCallback: (Long) -> Unit, private val binding: ActivityPlayBinding) {

    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private var isRunning = false
    private var elapsedTime = 0L
    private var updateRunnable: Runnable = object : Runnable {
        override fun run() {
            elapsedTime++
            updateCallback(elapsedTime)
            if (elapsedTime.toInt() >= 30) {
                onThirtySecondsReached()
            }
            mainThreadHandler.postDelayed(this, AppPreferencesKeys.ONE_SECOND)
        }
    }

    private fun onThirtySecondsReached() {
        killTimer()
    }

    fun start() {
        if (!isRunning) {
            isRunning = true
            mainThreadHandler.post(updateRunnable)
        }
    }

    fun updateCallback(elapsedTime: Long) {
        binding.trackTime.text = formatTrackDuration(elapsedTime * 1000)
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

    fun killTimer() {
        stop()
        reset()
        binding.trackTime.text = formatTrackDuration(0)
    }

    fun formatTrackDuration(duration: Long): CharSequence? {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
        val seconds =
            TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes)
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }
}