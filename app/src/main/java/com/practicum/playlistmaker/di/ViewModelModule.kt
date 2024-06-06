package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.medialibrary.ui.favorites.MLFavoritesViewModel
import com.practicum.playlistmaker.medialibrary.ui.playlists.MLCreatePlaylistViewModel
import com.practicum.playlistmaker.medialibrary.ui.playlists.MLPlaylistsViewModel
import com.practicum.playlistmaker.player.ui.PlayViewModel
import com.practicum.playlistmaker.search.ui.SearchViewModel
import com.practicum.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SettingsViewModel(get())
    }

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        PlayViewModel(get(), get(), get())
    }

    viewModel {
        MLFavoritesViewModel(get())
    }

    viewModel {
        MLPlaylistsViewModel(get())
    }

    viewModel {
        MLCreatePlaylistViewModel(get())
    }
}