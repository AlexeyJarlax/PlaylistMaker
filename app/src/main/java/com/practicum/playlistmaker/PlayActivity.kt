package com.practicum.playlistmaker

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.util.AppPreferencesKeys

class PlayActivity : AppCompatActivity() {

    private lateinit var btnBackFromSettings: Button
    private lateinit var btnPlay: ImageView
    private lateinit var btnAddToPlaylist: ImageView
    private lateinit var btnLike: ImageView
    private var isPlaying: Boolean = false
    private var isAddedToPlaylist: Boolean = false
    private var isLiked: Boolean = false
    private var count = 0 // заплатка на решение проблемы с появлением Тоста в момент прожатия кнопки
    // в начале активити. Простое решение для проблемы, которая требует детальной проработки
    // в следующих спринтах
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
    private var playerState = STATE_DEFAULT
//    private lateinit var play: Button
    private var mediaPlayer = MediaPlayer()
    private var url: String? = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview112/v4/ac/c7/d1/acc7d13f-6634-495f-caf6-491eccb505e8/mzaf_4002676889906514534.plus.aac.p.m4a"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
//        play = findViewById(R.id.btn_play)

        // Получение данных из Intent
        val trackName = intent.getStringExtra("trackName")
        val artistName = intent.getStringExtra("artistName")
        val trackTimeMillis = intent.getLongExtra("trackTimeMillis", 0)
        val artworkUrl100 = intent.getStringExtra("artworkUrl100")
        val collectionName = intent.getStringExtra("collectionName")
        val releaseDate = intent.getStringExtra("releaseDate")
        val primaryGenreName = intent.getStringExtra("primaryGenreName")
        val country = intent.getStringExtra("country")

        // Текстово-активичное
        findViewById<TextView>(R.id.track_name).text = trackName
        findViewById<TextView>(R.id.artist_name).text = artistName
        findViewById<TextView>(R.id.track_time).text = formatTrackDuration(trackTimeMillis)
        findViewById<TextView>(R.id.content1).text = formatTrackDuration(trackTimeMillis)
        findViewById<TextView>(R.id.content2).text = collectionName
        findViewById<TextView>(R.id.content3).text = releaseDate
        findViewById<TextView>(R.id.content4).text = primaryGenreName
        findViewById<TextView>(R.id.content5).text = country

        if (artworkUrl100 != null) { // пикча
            val artworkUrl512 = artworkUrl100.replace("100x100bb.jpg", "512x512bb.jpg")
            loadImage(artworkUrl512, findViewById(R.id.track_cover))
        }

        // Кнопочно-активичное
        btnBackFromSettings = findViewById(R.id.button_back_from_settings)
        btnPlay = findViewById(R.id.btn_play)
        btnAddToPlaylist = findViewById(R.id.btn_add_to_playlist)
        btnLike = findViewById(R.id.btn_like)
        setupBackButton()

        setupAddToPlaylistButton()
        setupLikeButton()

        // Подготовка к работе обработчиков
        btnPlay.performClick()
        btnAddToPlaylist.performClick()
        btnLike.performClick()
        count += 1

        // Воспроизведение плеером
        preparePlayer()
        setupPlayButton()
//        play.setOnClickListener {
//            playbackControl()
//        }
    } // конец onCreate

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
//                btnPlay.setImageResource(R.drawable.ic_btn_play_done)
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
//                btnPlay.setImageResource(R.drawable.ic_btn_play)
            }
        }
    }

    private fun setupPlayButton() {
        btnPlay.setOnClickListener {
            playbackControl()
//            val newImageResource = if (isPlaying) {
//                R.drawable.ic_btn_play_done
//            } else {
//                R.drawable.ic_btn_play
//            }
//            btnPlay.setImageResource(newImageResource as Int)
//            isPlaying = !isPlaying
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            btnPlay.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            btnPlay.setImageResource(R.drawable.ic_btn_play)
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        btnPlay.setImageResource(R.drawable.ic_btn_play_done)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        btnPlay.setImageResource(R.drawable.ic_btn_play)
        playerState = STATE_PAUSED
    }


private fun formatTrackDuration(trackTimeMillis: Long): String {
        val minutes = java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(trackTimeMillis)
        val seconds =
            java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(trackTimeMillis) - java.util.concurrent.TimeUnit.MINUTES.toSeconds(
                minutes
            )
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun loadImage(imageUrl: String, imageView: ImageView) {
        Glide.with(imageView).load(imageUrl).placeholder(R.drawable.ic_placeholder)
            .transform(RoundedCorners(AppPreferencesKeys.ALBUM_ROUNDED_CORNERS))
            .error(R.drawable.ic_placeholder)
            .into(imageView)
    }

    private fun setupBackButton() {
        btnBackFromSettings.setOnClickListener {
            finish()
        }
    }

    private fun setupAddToPlaylistButton() {
        btnAddToPlaylist.setOnClickListener {

            val newImageResource = if (isAddedToPlaylist) {
                count -=1
                R.drawable.ic_btn_add_to_playlist
            } else {
                count +=1
                R.drawable.ic_btn_add_to_playlist_done
            }
            if (count==2) {
                showSnackbar("Плейлист «BeSt SoNg EvEr!» создан")
            }
            btnAddToPlaylist.setImageResource(newImageResource)
            isAddedToPlaylist = !isAddedToPlaylist
        }
    }

    private fun setupLikeButton() {
        btnLike.setOnClickListener {
            val newImageResource = if (isLiked) {
                R.drawable.ic_btn_like_done
            } else {
                R.drawable.ic_btn_like
            }
            btnLike.setImageResource(newImageResource)
            isLiked = !isLiked
        }
    }

    private fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(
            findViewById(R.id.title3),
            message,
            Snackbar.LENGTH_SHORT
        )
        val snackbarView = snackbar.view
        snackbarView.setBackgroundResource(R.color.yp_black_and_yp_white)
        snackbar.show()
    }
}