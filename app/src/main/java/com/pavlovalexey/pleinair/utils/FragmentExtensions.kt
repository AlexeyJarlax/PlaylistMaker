package com.practicum.playlistmaker.utils

import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.R

//****************************************** индикатор загрузки и поток на продолжительные операции

fun Fragment.showSnackbar(message: String) {
    val snackbar = Snackbar.make(
        requireView(),
        message,
        Snackbar.LENGTH_SHORT
    )
    val snackbarView = snackbar.view
    snackbarView.setBackgroundResource(R.color.yp_black_and_yp_white)
    snackbar.show()
}

fun Fragment.startLoadingIndicator() {
    val loadingIndicator = requireActivity().findViewById<ProgressBar>(R.id.loading_indicator)
    loadingIndicator?.visibility = View.VISIBLE
}

fun Fragment.stopLoadingIndicator() {
    activity?.runOnUiThread {
        val loadingIndicator = requireActivity().findViewById<ProgressBar>(R.id.loading_indicator)
        loadingIndicator?.visibility = View.INVISIBLE
    }
}