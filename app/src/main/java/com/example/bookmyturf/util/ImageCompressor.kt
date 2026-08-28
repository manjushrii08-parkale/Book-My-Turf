package com.example.bookmyturf.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max

object ImageCompressor {

    private const val MIN_SIZE_KB = 100
    private const val MAX_SIZE_KB = 5120

    private const val TARGET_SIZE_KB = 800

    fun validateOriginalImage(
        context: Context,
        uri: Uri
    ): String? {

        val sizeBytes =
            context.contentResolver
                .openAssetFileDescriptor(uri, "r")
                ?.use {
                    it.length
                }
                ?: return "Unable to read image."

        val sizeKb =
            sizeBytes / 1024

        return when {

            sizeKb < MIN_SIZE_KB ->
                "Image is too small. Please select an image of at least 100 KB."

            sizeKb > MAX_SIZE_KB ->
                "Image is too large. Maximum allowed size is 5 MB."

            else -> null
        }
    }

    fun compress(
        context: Context,
        uri: Uri
    ): File? {

        val inputStream =
            context.contentResolver
                .openInputStream(uri)
                ?: return null

        val originalBitmap =
            BitmapFactory.decodeStream(inputStream)

        inputStream.close()

        originalBitmap ?: return null

        val resizedBitmap =
            resizeBitmap(
                originalBitmap,
                maxWidth = 1600,
                maxHeight = 1200
            )

        var quality = 85

        var compressedBytes: ByteArray

        do {

            val outputStream =
                ByteArrayOutputStream()

            resizedBitmap.compress(
                Bitmap.CompressFormat.JPEG,
                quality,
                outputStream
            )

            compressedBytes =
                outputStream.toByteArray()

            outputStream.close()

            quality -= 5

        } while (
            compressedBytes.size / 1024 > TARGET_SIZE_KB &&
            quality >= 40
        )

        val file =
            File(
                context.cacheDir,
                "turf_${System.currentTimeMillis()}.jpg"
            )

        FileOutputStream(file).use {

            it.write(compressedBytes)
        }

        if (resizedBitmap != originalBitmap) {
            resizedBitmap.recycle()
        }

        originalBitmap.recycle()

        return file
    }

    private fun resizeBitmap(
        bitmap: Bitmap,
        maxWidth: Int,
        maxHeight: Int
    ): Bitmap {

        val width =
            bitmap.width

        val height =
            bitmap.height

        if (
            width <= maxWidth &&
            height <= maxHeight
        ) {
            return bitmap
        }

        val widthRatio =
            maxWidth.toFloat() / width

        val heightRatio =
            maxHeight.toFloat() / height

        val ratio =
            minOf(
                widthRatio,
                heightRatio
            )

        val newWidth =
            (width * ratio).toInt()

        val newHeight =
            (height * ratio).toInt()

        return Bitmap.createScaledBitmap(
            bitmap,
            newWidth,
            newHeight,
            true
        )
    }
}