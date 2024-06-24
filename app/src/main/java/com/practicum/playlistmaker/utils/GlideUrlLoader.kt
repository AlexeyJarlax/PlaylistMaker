package com.practicum.playlistmaker.utils

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

internal class GlideUrlLoader(private val placeholderResourceId: Int) {

    fun loadImage(imageUrl: String?, imageView: ImageView) {
        val uri = imageUrl?.let { Uri.parse(it) }
        loadImage(uri, imageView)
    }

    fun loadImage(imageUrl: Uri?, imageView: ImageView) {
        Glide.with(imageView)
            .load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(placeholderResourceId)
            .fallback(placeholderResourceId)
            .transform(RoundedCorners(AppPreferencesKeys.ALBUM_ROUNDED_CORNERS))
            .error(placeholderResourceId)
            .centerCrop()
            .into(imageView)
    }
}