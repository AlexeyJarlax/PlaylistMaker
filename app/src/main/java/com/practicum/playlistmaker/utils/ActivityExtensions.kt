package com.practicum.playlistmaker.utils

import android.app.Activity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.practicum.playlistmaker.R
import kotlin.concurrent.thread

fun Activity.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}


fun Activity.buttonToGoBack() { // КНОПКА НАЗАД
    val back = findViewById<Button>(R.id.buttonBack)
    back?.setDebouncedClickListener {
        finish()
    }
}

//****************************************** индикатор загрузки и поток на продолжительные операции
fun Activity.startLoadingIndicator() {
    val loadingIndicator = findViewById<ProgressBar>(R.id.loading_indicator)
    loadingIndicator?.visibility = View.VISIBLE
}

fun Activity.stopLoadingIndicator() {
    val loadingIndicator = findViewById<ProgressBar>(R.id.loading_indicator)
    loadingIndicator?.visibility = View.INVISIBLE
}

fun Activity.openThread(onThreadComplete: () -> Unit) {
    startLoadingIndicator()
    thread {
        onThreadComplete()
    }
}

//****************************************** решаем проблемы с загрузкой и списком треков

fun Activity.solvingThisProblemWith(problemTipo: String, sendRequestForDoReserch: () -> Unit) {
    val utilErrorBox = findViewById<LinearLayout>(R.id.utilErrorBox)
    val errorIcon = findViewById<ImageView>(R.id.error_icon)
    val errorTextWeb = findViewById<TextView>(R.id.error_text_web)
    val retryButton = findViewById<Button>(R.id.retry_button)

    when (problemTipo) {
        AppPreferencesKeys.INTERNET -> {
            errorIcon.setImageResource(R.drawable.ic_error_internet)
            errorTextWeb.text = resources.getString(R.string.error_text_web)
            retryButton.visibility = View.VISIBLE
            retryButton.setDebouncedClickListener {
                sendRequestForDoReserch() // тут отправляем на повторный поиск
                utilErrorBox.visibility = View.GONE
            }
        }

        AppPreferencesKeys.RESULTS -> {
            errorIcon.setImageResource(R.drawable.ic_error_notfound)
            errorTextWeb.text = resources.getString(R.string.nothing_was_found)
            retryButton.visibility = View.GONE
        }

        else -> {
            retryButton.visibility = View.GONE
        }
    }

    utilErrorBox.visibility = View.VISIBLE
    utilErrorBox.setDebouncedClickListener {
        utilErrorBox.visibility = View.GONE
    }
}



