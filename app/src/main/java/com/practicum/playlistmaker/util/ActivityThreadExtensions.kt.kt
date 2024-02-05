package com.practicum.playlistmaker.util

import android.app.Activity
import android.view.View
import android.widget.ProgressBar
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.practicum.playlistmaker.R
import kotlin.concurrent.thread

fun Activity.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

//****************************************** индикатор загрузки и поток на длинные конечные операции
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


