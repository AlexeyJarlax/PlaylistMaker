package com.practicum.playlistmaker.utils

import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R

//****************************************** индикатор загрузки и поток на продолжительные операции

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