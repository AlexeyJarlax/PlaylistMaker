package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.medialibrary.ui.favorites.FavoritesViewModel
import com.practicum.playlistmaker.medialibrary.ui.newplaylist.NewPlaylistViewModel
import com.practicum.playlistmaker.medialibrary.ui.playlist.PlaylistViewModel
import com.practicum.playlistmaker.player.ui.PlayerViewModel
import com.practicum.playlistmaker.search.domain.models.Track
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

    viewModel { (track: Track) ->
        PlayerViewModel(get(), get(), get(), track)
    }

    viewModel {
        FavoritesViewModel(get(), get())
    }

    viewModel {
        PlaylistViewModel(get())
    }

    viewModel{
        NewPlaylistViewModel(get())
    }
}