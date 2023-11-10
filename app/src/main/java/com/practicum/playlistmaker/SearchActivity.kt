package com.practicum.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*

class SearchActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var clearButton: ImageButton
    private lateinit var searchHistoryListView: ListView
    private lateinit var searchHistoryAdapter: ArrayAdapter<String>
    private lateinit var searchHistorySet: HashSet<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchEditText = findViewById(R.id.search_edit_text)
        clearButton = findViewById(R.id.clearButton)
        searchHistoryListView = findViewById(R.id.search_history_list_view)

        val back = findViewById<Button>(R.id.button_back_from_search_activity) // КНОПКА НАЗАД
        back.setOnClickListener {
            finish()
        }

        // Считываем сохраненный набор поисковых запросов
        val preferences: SharedPreferences =
            getSharedPreferences("SearchHistory", Context.MODE_PRIVATE)
        searchHistorySet = preferences.getStringSet("search_history", HashSet()) as HashSet<String>

        val searchHistoryList = ArrayList(searchHistorySet)

        // Создаем ArrayAdapter для отображения списка поисковых запросов
        searchHistoryAdapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, searchHistoryList)
        searchHistoryListView.adapter = searchHistoryAdapter

        // Обработчик клика по элементу списка
        searchHistoryListView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedQuery = parent.getItemAtPosition(position) as String
                searchEditText.setText(selectedQuery)
            }
        // Обработчик ввода
        searchEditText.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL ||
                (keyEvent != null && keyEvent.action == KeyEvent.ACTION_DOWN && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
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

        // Обработчик очистки ввода
        clearButton.setOnClickListener {
            searchEditText.text.clear()
            clearButton.visibility = View.GONE
            hideKeyboard()
        }

        // Обновление видимости кнопки очистки
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateClearButtonVisibility(s?.isNotEmpty() ?: false)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun updateClearButtonVisibility(isVisible: Boolean) {
        clearButton.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    // Обработчик сокрытие клавы
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

    // Обработчик сейва поисковых запросов
    private fun saveSearchQuery(query: String) {
        searchHistorySet.add(query)
        searchHistoryAdapter.add(query)
        searchHistoryAdapter.notifyDataSetChanged()

        val preferences: SharedPreferences =
            getSharedPreferences("SearchHistory", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putStringSet("search_history", searchHistorySet)
        editor.apply()
    }
}