package com.practicum.playlistmaker.data.network

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R

internal class CommunicationButtons(private val context: Context) {
    fun buttonShare() {
        val appId = "com.Practicum.PlaylistMaker"
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(
            Intent.EXTRA_TEXT,
            context.getString(R.string.share_app_text, appId)
        )
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_app_title)))
    }

   fun buttonHelp() {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(context.getString(R.string.support_email))
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.support_email_subject))
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.support_email_text))
            context.startActivity(this)
        }
    }
}