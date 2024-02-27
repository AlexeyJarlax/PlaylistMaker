//package com.practicum.playlistmaker.player.garbage__sprint16
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
//import androidx.lifecycle.viewmodel.initializer
//import androidx.lifecycle.viewmodel.viewModelFactory
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import com.practicum.playlistmaker.creator.MyApplication
//
//
//class TrackViewModel(
//    private val trackId: String,
//    private val tracksInteractor: TracksInteractor,
//    private val trackPlayer: TrackPlayer,
//) : ViewModel() {
//
//    private val screenStateLiveData = MutableLiveData<TrackScreenState>(TrackScreenState.Loading)
//    private val playStatusLiveData = MutableLiveData<PlayStatus>()
//
//    init {
//        tracksInteractor.loadTrackData(
//            trackId = trackId,
//            onComplete = { trackModel ->
//                screenStateLiveData.postValue(
//                    TrackScreenState.Content(trackModel)
//                )
//            }
//        )
//    }
//
//    fun getScreenStateLiveData(): LiveData<TrackScreenState> = screenStateLiveData
//    fun getPlayStatusLiveData(): LiveData<PlayStatus> = playStatusLiveData
//
//    fun play() {
//        trackPlayer.play(
//            trackId = trackId,
//            // 1
//            statusObserver = object : TrackPlayer.StatusObserver {
//                override fun onProgress(progress: Float) {
//                    // 2
//                    playStatusLiveData.value = getCurrentPlayStatus().copy(progress = progress)
//                }
//
//                override fun onStop() {
//                    // 3
//                    playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = false)
//                }
//
//                override fun onPlay() {
//                    // 4
//                    playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = true)
//                }
//            },
//        )
//    }
//
//    // 5
//    fun pause() {
//        trackPlayer.pause(trackId)
//    }
//
//    // 6
//    override fun onCleared() {
//        trackPlayer.release(trackId)
//    }
//
//    private fun getCurrentPlayStatus(): PlayStatus {
//        return playStatusLiveData.value ?: PlayStatus(progress = 0f, isPlaying = false)
//    }
//
//    companion object {
//        fun getViewModelFactory(trackId: String): ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                val myApp = this[APPLICATION_KEY] as MyApplication
//                val interactor = myApp.provideTracksInteractor()
//                val trackPlayer = myApp.provideTrackPlayer()
//
//                TrackViewModel(
//                    trackId,
//                    interactor,
//                    trackPlayer,
//                )
//            }
//        }
//    }
//}