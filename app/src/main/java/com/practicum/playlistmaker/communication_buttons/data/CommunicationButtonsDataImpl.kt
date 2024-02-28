package com.practicum.playlistmaker.communication_buttons.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.communication_buttons.domain.CommunicationButtonsData


class CommunicationButtonsDataImpl(private val context: Context) : CommunicationButtonsData {

    override fun buttonToShareApp() {
        val appId = "com.Practicum.PlaylistMaker"
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(
            Intent.EXTRA_TEXT,
            context.getString(R.string.share_app_text, appId)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val chooserIntent = Intent.createChooser(intent, context.getString(R.string.share_app_title))
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooserIntent)
    }

    override fun buttonToHelp() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse(context.getString(R.string.support_email))
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.support_email_subject))
        intent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.support_email_text))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }


    override fun buttonToSeeUserAgreement() {
        val url = context.getString(R.string.user_agreement_url)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

}