package com.practicum.playlistmaker.utils

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

import com.practicum.playlistmaker.R

internal class ArtworkUrlLoader {

    fun loadImage(imageUrl: String?, imageView: ImageView) {
        val uri = imageUrl?.let { Uri.parse(it) }
        loadImage(uri, imageView)
    }

    fun loadImage(imageUrl: Uri?, imageView: ImageView) {
        Glide.with(imageView).load(imageUrl).placeholder(R.drawable.ic_placeholder)
            .transform(RoundedCorners(AppPreferencesKeys.ALBUM_ROUNDED_CORNERS))
            .error(R.drawable.ic_placeholder)
            .into(imageView)
    }
}