package com.practicum.playlistmaker.creator

import android.app.Application
import com.practicum.playlistmaker.main.domain.ThemeManager

class PlaylistMaker : Application()  {
    override fun onCreate() {
        super.onCreate()
        ThemeManager.applyTheme(this)
    }
}