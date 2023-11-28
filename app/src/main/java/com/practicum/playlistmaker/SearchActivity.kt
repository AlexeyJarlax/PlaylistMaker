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
    private lateinit var searchHistorySpinner: Spinner
    private lateinit var searchHistoryAdapter: ArrayAdapter<String>
    private lateinit var searchHistoryList: ArrayList<String>
    private lateinit var searchHistorySet: HashSet<String>
    private lateinit var trackRecyclerView: RecyclerView
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var trackAdapter: TrackAdapter
    private var shouldUpdateSearchField = true
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
        setupHistorySpinner()
        setupPicturesDayNightForm()
        setupTrackRecyclerViewAndTrackAdapter()
    }

    private fun setupViews() {
        clearButton = findViewById(R.id.clearButton)
        searchHistorySpinner = findViewById(R.id.search_history_spinner)
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
//                updateClearButtonVisibility(searchText.isNotEmpty())  // кнопка Х будет для сброса поиска
                if (searchText.isEmpty()) {
                    trackAdapter.updateList(originalTracks)
                    trackRecyclerView.scrollToPosition(0)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
}
    private fun updateClearButtonVisibility(isVisible: Boolean) {
        clearButton.visibility = if (isVisible) View.VISIBLE else View.GONE
    } // пока не нужно


    private fun backToMain() { // кнопка НАЗАД
        val backButton = findViewById<Button>(R.id.button_back_from_search_activity)
        backButton.setOnClickListener {
            finish()
        }}

    private fun setupHistorySpinner() { // заполнение спинера
        val preferences: SharedPreferences =
            getSharedPreferences(PREF_SEARCH_HISTORY, Context.MODE_PRIVATE)
        val historySet = preferences.getStringSet(PREF_KEY_SEARCH_HISTORY, null)

        if (historySet != null) { // Чек на тип значения historySet
            if (historySet is HashSet<*>) {
                searchHistorySet = HashSet(historySet)
            } else {
                val message = "ошибка searchHistorySet"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                searchHistorySet = HashSet()
            }
        } else {
            searchHistorySet = HashSet()
        }

        searchHistoryList = ArrayList(searchHistorySet)

        searchHistoryAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, searchHistoryList)
        searchHistoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        searchHistorySpinner.adapter = searchHistoryAdapter

        searchHistorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (shouldUpdateSearchField) {
                    val selectedQuery = parent.getItemAtPosition(position) as String
                    searchEditText.setText(selectedQuery)
                }
                shouldUpdateSearchField = true
                parent.getChildAt(0)?.findViewById<TextView>(android.R.id.text1)?.visibility =
                    View.GONE
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                //
            }
        }
    }

    private fun setupPicturesDayNightForm() { // цветовая настройка День\ночь на две иконки (Лупа и Х) внутри окна поиска иконки
        val searchIcon: ImageButton = findViewById(R.id.search_icon)
        searchIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_search))
        val clearButton: ImageButton = findViewById(R.id.clearButton)
        clearButton.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_clear_light_mode
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
        trackAdapter = TrackAdapter(this, originalTracks, originalTracks) { webUrl ->
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

    private fun preparingForSearch(searchText: String) { // Подготовка поиска: Индикатор, отключение кнопок на момент поиска, performSearch, задержка и запуск сохранения списка
        loadingIndicator.visibility = View.VISIBLE
        clearButton.isEnabled = false
//        searchHistorySpinner.isEnabled = false
        Handler(Looper.getMainLooper()).postDelayed({
            performSearch(searchText) // поиск
            saveSearchQuery(searchText)
            loadingIndicator.visibility = View.GONE
            clearButton.isEnabled = true
//            searchHistorySpinner.isEnabled = true
            updatePage()
        }, 500)
    }

    private fun updatePage() {
        searchHistoryAdapter?.notifyDataSetChanged() // тут должно быть что-то важное, пока не понял что
    }
    private fun performSearch(query: String) {  // Попытка сделать вумный поиск
        val filteredTracks = originalTracks.filter { track ->
            track.trackName.contains(query, ignoreCase = true) || track.artistName.contains(query, ignoreCase = true)
        }.toMutableList() // Преобразование к изменяемому списку
        sortedTracks.clear() // Очистка отсортированного списка
        sortedTracks.addAll(filteredTracks.sortedBy { it.trackName }) // Добавляем отфильтрованные и отсортированные треки в sortedTracks
        trackAdapter.updateList(sortedTracks) // Обновляем список треков в адаптере
        trackRecyclerView.scrollToPosition(0) // Скроллим к началу списка

    }

    private fun saveSearchQuery(searchText: String) {
        searchHistorySet.add(searchText)
        searchHistoryList = ArrayList(searchHistorySet)
        searchHistoryAdapter.clear()
        searchHistoryAdapter.addAll(searchHistoryList)
        searchHistoryAdapter.notifyDataSetChanged()

        val preferences: SharedPreferences =
            getSharedPreferences(PREF_SEARCH_HISTORY, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putStringSet(PREF_KEY_SEARCH_HISTORY, searchHistorySet)
        editor.apply()

        shouldUpdateSearchField = false
    }

    override fun onStop() { // Очищаем историю запросов при выходе
        super.onStop()

        val preferences: SharedPreferences =
            getSharedPreferences(PREF_SEARCH_HISTORY, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.remove(PREF_KEY_SEARCH_HISTORY)
        editor.apply()
    }
}
