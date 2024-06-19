package com.practicum.playlistmaker.medialibrary.ui.openplaylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

import com.practicum.playlistmaker.medialibrary.domain.model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.others.PlaylistsInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.utils.changeRussianWordsAsTracks
import com.practicum.playlistmaker.utils.changeRussianWordsAsMinutes
import com.practicum.playlistmaker.utils.msToMm
import com.practicum.playlistmaker.utils.msToSs

class OpenPlaylistViewModel(
    private val playlistId: Int?,
    private val playlistsInteractor: PlaylistsInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private val statePlaylistLiveData = MutableLiveData<OpenPlaylistState>()
    fun observeState(): LiveData<OpenPlaylistState> = statePlaylistLiveData

    private var listTrack: List<Track> = mutableListOf()

    private var playlist: Playlist? = null

    init {
        viewModelScope.launch {
            getData()
        }
    }

    private fun getData() {
        viewModelScope.launch {
            if (playlistId != null) {
                playlist = playlistsInteractor.getPlaylistById(playlistId)

                val list: MutableList<Track> = mutableListOf()
                playlist?.tracksIds?.map {
                    list.add(playlistsInteractor.getTrackById(it))
                }

                list.reverse()
                listTrack = list
                preparingPlaylistData()
            }
        }
    }

    fun updatePlaylist() {
        getData()
    }

    private fun preparingPlaylistData() {
        if (playlist != null) {
            statePlaylistLiveData.postValue(
                OpenPlaylistState.Content(
                    playlist?.urlImage,
                    playlist!!.playlistName,
                    playlist?.description,
                    getDurationAllTrack(listTrack),
                    listTrack.size.toString() + " " +
                            changeRussianWordsAsTracks(listTrack.size),
                    listTrack
                )
            )
        }
    }


    fun deletePlaylist() {
        viewModelScope.launch {
            if (playlistId != null) {
                playlistsInteractor.deletePlaylist(playlistId)
                statePlaylistLiveData.postValue(OpenPlaylistState.Delete)
            }
        }
    }

    private fun getDurationAllTrack(tracksInPlaylist: List<Track>?): String {
        var duration = 0
        viewModelScope.launch {
            tracksInPlaylist?.map {
                duration += it.trackTimeMillis?.toInt() ?: 0
            }
        }
        return msToMm(duration.toString()) + " " + changeRussianWordsAsMinutes(duration)
    }

    fun deleteTrackFromPlaylist(trackId: Int) {
        viewModelScope.launch {
            val playlist = playlistsInteractor.getPlaylistById(playlistId!!)
            playlistsInteractor.updatePlaylistAndDeleteTrack(
                trackId,
                playlist
            )
            getData()
            if (listTrack.size <= 1) {
                statePlaylistLiveData.postValue(OpenPlaylistState.Delete)
            } else {
                preparingPlaylistData()
            }
        }
    }

    fun sharePlaylist() {
        viewModelScope.launch {
            if (playlistId != null) {
                val list: StringBuilder = StringBuilder()
                listTrack.mapIndexed { index: Int, track: Track ->
                    list.append("${index + 1}. ${track.artistName} - ${track.trackName} (${msToSs(track.trackTimeMillis)})\n")
                }
                val messages = "${playlist?.playlistName}\n" +
                        "${playlist?.description}\n" +
                        "[${playlist?.tracksCount}] ${changeRussianWordsAsTracks(playlist?.tracksCount ?: 0)}\n" +
                        list
                settingsInteractor.sharePlaylist(messages)
            }
        }
    }
}