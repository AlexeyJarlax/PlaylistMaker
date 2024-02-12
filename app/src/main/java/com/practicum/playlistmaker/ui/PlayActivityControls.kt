package com.practicum.playlistmaker.ui

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.impl.SecondsCounter

class PlayActivityControls(
    private val secondsCounter: SecondsCounter,
    private val mediaPlayer: MediaPlayer,
    private val playActivity: PlayActivity,
) {

}