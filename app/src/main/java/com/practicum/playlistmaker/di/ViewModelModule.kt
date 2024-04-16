package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.main.ui.MainViewModel
import com.practicum.playlistmaker.medialibrary.favorites.MLFavoritesViewModel
import com.practicum.playlistmaker.medialibrary.playlists.MLPlaylistsViewModel
import com.practicum.playlistmaker.player.ui.PlayViewModel
import com.practicum.playlistmaker.search.ui.SearchViewModel
import com.practicum.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MainViewModel()
    }

    viewModel {
        SettingsViewModel(get())
    }

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        PlayViewModel(get())
    }

    viewModel {
        MLFavoritesViewModel(get())
    }

    viewModel {
        MLPlaylistsViewModel(get())
    }
}