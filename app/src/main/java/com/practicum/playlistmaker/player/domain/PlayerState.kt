package com.practicum.playlistmaker.player.domain

enum class PlayerState {
    INITIAL,
    READY,
    PLAYING,
    PAUSED,
    KILL,
    ERROR
}