package com.practicum.playlistmaker.utils

import android.app.Activity
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R

fun Activity.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}


fun Activity.bindGoBackButton() { // КНОПКА НАЗАД
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

fun Fragment.toast(text: String) {
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
}

fun Fragment.startLoadingIndicator() {
    val loadingIndicator = requireActivity().findViewById<ProgressBar>(R.id.loading_indicator)
    loadingIndicator?.visibility = View.VISIBLE
}

fun Fragment.stopLoadingIndicator() {
    val loadingIndicator = requireActivity().findViewById<ProgressBar>(R.id.loading_indicator)
    loadingIndicator?.visibility = View.INVISIBLE
}