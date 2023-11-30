package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.util.Track
import com.practicum.playlistmaker.util.TrackAdapter
import com.practicum.playlistmaker.util.arrayTrackList

class SearchActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var clearButton: ImageButton
    private lateinit var trackRecyclerView: RecyclerView
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var trackAdapter: TrackAdapter
    private val sortedTracks = mutableListOf<Track>()
    private val originalTracks = mutableListOf<Track>()

    companion object {
        private const val PREF_SEARCH_HISTORY = "SearchHistory"
        private const val PREF_KEY_SEARCH_HISTORY = "search_history"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        originalTracks.addAll(arrayTrackList)
        setupViews()
        backToMain()
        setupPicturesDayNightForm()
        setupTrackRecyclerViewAndTrackAdapter()
    }

    private fun setupViews() {
        clearButton = findViewById(R.id.clearButton)
        searchEditText = findViewById(R.id.search_edit_text)
        loadingIndicator = findViewById(R.id.loading_indicator)
        loadingIndicator.visibility = View.GONE

        clearButton.setOnClickListener {
            searchEditText.text.clear()
            clearButton.visibility = View.GONE
            trackAdapter.updateList(originalTracks)
        }

        searchEditText.setOnEditorActionListener { textView, actionId, keyEvent -> // заполнение с виртуальной клавиатуры
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val searchText = searchEditText.text.toString().trim()
                if (searchText.isNotEmpty()) {
                    preparingForSearch(searchText)
                    showToast("Мы пошли искать: $searchText")
                }
                clearSearchFieldAndHideKeyboard()
                true
            } else {
                false
            }
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().trim()
                if (searchText.isEmpty()) {
                    trackAdapter.updateList(originalTracks)
                    trackRecyclerView.scrollToPosition(0)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun backToMain() { // кнопка НАЗАД
        val backButton = findViewById<Button>(R.id.button_back_from_search_activity)
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun setupPicturesDayNightForm() { // цветовая настройка День\ночь на две иконки (Лупа и Х) внутри окна поиска иконки
        val searchIcon: ImageButton = findViewById(R.id.search_icon)
        searchIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_search))
        val clearButton: ImageButton = findViewById(R.id.clearButton)
        clearButton.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_clear
            )
        )
        val attributes = obtainStyledAttributes(intArrayOf(R.attr.iconColor, R.attr.iconPath))
        val iconColor =
            attributes.getColor(0, ContextCompat.getColor(this, R.color.yp_text_gray__yp_black))
        val iconPath = attributes.getString(1)
        attributes.recycle()
        searchIcon.setColorFilter(iconColor)
        searchIcon.tag = iconPath
        clearButton.setColorFilter(iconColor)
        clearButton.tag = iconPath
    }

    private fun setupTrackRecyclerViewAndTrackAdapter() {
        trackRecyclerView = findViewById(R.id.track_recycler_view)
        val layoutManager = LinearLayoutManager(this)
        trackAdapter = TrackAdapter(this, originalTracks) { webUrl ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webUrl))
            startActivity(intent)
        }
        trackRecyclerView.layoutManager = layoutManager
        trackRecyclerView.adapter = trackAdapter
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
    }

    private fun clearSearchFieldAndHideKeyboard() {
        searchEditText.text.clear()
        hideKeyboard()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun preparingForSearch(searchText: String) { // Подготовка поиска
        loadingIndicator.visibility = View.VISIBLE
        clearButton.isEnabled = false
        Handler(Looper.getMainLooper()).postDelayed({
            performSearch(searchText) // поиск
//            saveSearchQuery(searchText)
            loadingIndicator.visibility = View.GONE
            clearButton.isEnabled = true
        }, 500)
    }

    private fun performSearch(query: String) {
        val filteredTracks = originalTracks.filter { track ->
            track.trackName.contains(query, ignoreCase = true) || track.artistName.contains(
                query,
                ignoreCase = true
            )
        }.toMutableList()
        sortedTracks.clear()
        sortedTracks.addAll(filteredTracks.sortedBy { it.trackName })
        trackAdapter.updateList(sortedTracks)
    }

    override fun onStop() { // Досвидули
        super.onStop()
        val preferences: SharedPreferences =
            getSharedPreferences(PREF_SEARCH_HISTORY, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.remove(PREF_KEY_SEARCH_HISTORY)
        editor.apply()
    }
}
