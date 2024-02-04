package com.practicum.playlistmaker.util

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ProgressBar
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import kotlinx.coroutines.withContext
import android.content.Context
import android.view.Gravity
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.practicum.playlistmaker.R
import kotlin.concurrent.thread

//fun Activity.showManualKeyInputDialog(thisTitle: String, onKeyEntered: (String) -> Unit) {
//    val inputEditText = EditText(this)
//    val builder = AlertDialog.Builder(this)
//
//    val titleTextView = TextView(this)
//    titleTextView.text = thisTitle
//    titleTextView.setPadding(10, 10, 10, 10) // Add padding if needed
//    titleTextView.gravity = Gravity.CENTER
//
//    builder.setCustomTitle(titleTextView)
//    builder.setView(inputEditText)
//
//    builder.setPositiveButton("✔️") { _, _ ->
//        val enteredKey = inputEditText.text.toString()
//        onKeyEntered(enteredKey)
//    }
//
//    builder.setNegativeButton("❌") { dialog, _ ->
//        dialog.cancel()
//    }
//
//    builder.show()
//}
//
//fun Activity.showYesNoDialog(thisTitle: String, onDeleteConfirmed: () -> Unit) {
//    val builder = AlertDialog.Builder(this)
//    builder.setTitle(thisTitle)
//    val titleTextView = TextView(this)
//    titleTextView.text = thisTitle
//
//    val digits = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9")
//
//    // Устанавливаем цифры как элементы списка
//    builder.setItems(digits) { _, which ->
//        val selectedDigit = digits[which]
//
//        // Проверяем, является ли выбранная цифра утвердительной
//        if (selectedDigit == "8") {
//            onDeleteConfirmed()
//        } else {
//            // В случае, если выбрана не та цифра, просто отменяем диалог
//            builder.create().cancel()
//        }
//    }
//
//    builder.show()
//}
//
//fun Activity.showLoadingIndicator() {
//    val buttonForCover2 = findViewById<View>(R.id.button_for_cover2) // кнопка заглушка экрана
//    val loadingIndicator2 = findViewById<ProgressBar>(R.id.loading_indicator2) // индикатора загрузки
//    buttonForCover2.visibility = View.VISIBLE
//    loadingIndicator2.visibility = View.VISIBLE
//}
//
//fun Activity.hideLoadingIndicator(cornerLeft: Boolean) {
//    val buttonForCover2 = findViewById<View>(R.id.button_for_cover2)
//    val loadingIndicator2 = findViewById<ProgressBar>(R.id.loading_indicator2)
//
//    val multiplier = if (cornerLeft) 3 else 1
//    Handler(Looper.getMainLooper()).postDelayed({
//        buttonForCover2.visibility = View.INVISIBLE
//        loadingIndicator2.visibility = View.INVISIBLE
//    }, APK.LOAD_PROCESSING_MILLISECONDS * multiplier)
//}
fun Activity.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

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


