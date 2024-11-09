package com.practicum.playlistmaker.medialibrary.data.db.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

import com.practicum.playlistmaker.medialibrary.domain.others.ImageStorage

class ImageStorageImpl(private val context: Context) : ImageStorage {

    override fun saveImageToPrivateStorage(uri: Uri): String {
        // Создание директории для сохранения изображений
        val filePath = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "krasavchik"
        )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        // Генерация уникального имени файла на основе текущего времени
        val imageName = Calendar.getInstance().time.toString()
        val file = File(filePath, imageName)

        // Открытие входного и выходного потоков для копирования изображения
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        // Сжатие и сохранение изображения в формате JPEG
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 40, outputStream)

        return imageName
    }

    override fun getImageFromPrivateStorage(imageName: String): Uri {
        // Получение пути к директории с изображениями
        val filePath = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "krasavchik"
        )
        val file = File(filePath, imageName)
        return file.toUri()
    }
}