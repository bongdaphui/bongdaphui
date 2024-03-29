package com.bongdaphui.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Environment

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object CompressFile {
    fun getCompressedImageFile(file: File, mContext: Context): File? {
        try {
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true

            if (getFileExt(file.name) == "png" || getFileExt(file.name) == "PNG") {
                o.inSampleSize = 6
            } else {
                o.inSampleSize = 6
            }

            var inputStream = FileInputStream(file)
            BitmapFactory.decodeStream(inputStream, null, o)
            inputStream.close()

            // The new size we want to scale to
            val REQUIRED_SIZE = 100

            // Find the correct scale value. It should be the power of 2.
            var scale = 1
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2
            }

            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            inputStream = FileInputStream(file)

            val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)

            val ei = ExifInterface(file.absolutePath)
            val orientation = ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )

            when (orientation) {

                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(selectedBitmap, 90f)

                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(selectedBitmap, 180f)

                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(selectedBitmap, 270f)

                ExifInterface.ORIENTATION_NORMAL -> {
                }

                else -> {
                }
            }
            inputStream.close()


            // here i override the original image file
            val folder = File(Environment.getExternalStorageDirectory().toString() + "/FolderName")
            var success = true
            if (!folder.exists()) {
                success = folder.mkdir()
            }
            if (success) {
                val newFile = File(File(folder.absolutePath), file.name)
                if (newFile.exists()) {
                    newFile.delete()
                }
                val outputStream = FileOutputStream(newFile)

                if (getFileExt(file.name) == "png" || getFileExt(file.name) == "PNG") {
                    selectedBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                } else {
                    selectedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }

                outputStream.flush()
                outputStream.close()
                return newFile
            } else {
                return null
            }
        } catch (e: Exception) {
            return null
        }

    }

    fun getFileExt(fileName: String): String {
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length)
    }

    fun rotateImage(source: Bitmap?, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source!!, 0, 0, source.width, source.height,
            matrix, true
        )
    }
}
