package com.practicum.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
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
    private var shouldUpdateSearchField = true

    companion object {
        private const val PREF_SEARCH_HISTORY = "SearchHistory"
        private const val PREF_KEY_SEARCH_HISTORY = "search_history"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // обработка кнопок меню поиска
        clearButton = findViewById(R.id.clearButton)
        searchHistorySpinner = findViewById(R.id.search_history_spinner)

        // обработка кнопки НАЗАД
        val back = findViewById<Button>(R.id.button_back_from_search_activity)
        back.setOnClickListener {
            finish()
        }

        // обработка инициализации списка песен\альбомов
        trackRecyclerView = findViewById(R.id.track_recycler_view)
        setupTrackRecyclerView()

//============================== ПОИСКОВАЯ СТРОКА ==============================//
        searchEditText = findViewById(R.id.search_edit_text)
        val searchIcon = findViewById<ImageButton>(R.id.search_icon)
        // чистим поисковую строку
        if (savedInstanceState == null) {
            searchEditText.setText("")
        }
        clearButton.visibility = View.GONE
        hideKeyboard()
        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                searchHistorySpinner.visibility = View.VISIBLE
            } else {
                searchHistorySpinner.visibility = View.GONE
            }
        }
// обработка клика по лупе
        searchIcon.setOnClickListener {
            val searchText = searchEditText.text.toString().trim()
            if (searchText.isNotEmpty()) {
                showToast("Мы пошли искать: $searchText")
                saveSearchQuery(searchText)
            }
            clearSearchFieldAndHideKeyboard()
        }
// обработка ввода
        searchEditText.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val searchText = searchEditText.text.toString().trim()
                if (searchText.isNotEmpty()) {
                    showToast("Мы пошли искать: $searchText")
                    saveSearchQuery(searchText)
                }
                clearSearchFieldAndHideKeyboard()
                true
            } else {
                false
            }
        }

        clearButton.setOnClickListener {
            searchEditText.text.clear()
            clearButton.visibility = View.GONE
            hideKeyboard()
        }

// считыватель ввода
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                 }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateClearButtonVisibility(s?.isNotEmpty() ?: false)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

//=========== ОБРАБОТКА Spinner с СОХРАНЕНИЕМ РЕЗУЛЬТАТА ПРОШЛЫХ ПОИСКОВ ====================//
        val preferences: SharedPreferences =
            getSharedPreferences(PREF_SEARCH_HISTORY, Context.MODE_PRIVATE)
        searchHistorySet =
            HashSet(preferences.getStringSet(PREF_KEY_SEARCH_HISTORY, HashSet()))
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
    private fun updateClearButtonVisibility(isVisible: Boolean) {
        clearButton.visibility = if (isVisible) View.VISIBLE else View.GONE
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

    private fun saveSearchQuery(query: String) {
        searchHistorySet.add(query)
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

    //=========== ОБРАБОТКА СПИСКА ПЕСЕН\АЛЬБОМОВ ====================//
    private fun setupTrackRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        val trackAdapter = TrackAdapter(this, arrayTrackList) { track ->
            // Обработка клика по треку на будущее
        }

        trackRecyclerView.layoutManager = layoutManager
        trackRecyclerView.adapter = trackAdapter
    }
}
