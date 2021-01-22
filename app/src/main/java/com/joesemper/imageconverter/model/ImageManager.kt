package com.joesemper.imageconverter.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.net.toUri
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception


// Захардкоженный адрес папки с фотографиями (программно получить не получилось).
private const val IMAGE_PATH = "/storage/emulated/0/DCIM/100ANDRO/"

// Имя конвертированного изображения
private const val CONVERTED_IMAGE_NAME = "converted"

class ImageManager {

    private var currentImage: File? = null
    private var convertedImage: File? = null

    /**
     * Находит изображение в папке с фотографиями и создает соответсвующий File.
     * Выполняется в потоке Schedulers.io()
     * @param name имя изображения
     */
    fun findImage(name: String): Completable = Completable.create { emitter ->
        createImageFile(name).let {
            if (it) {
                emitter.onComplete()
            } else {
                emitter.onError(Exception("Image not found"))
            }
        }
    }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())


    fun getCurrentImage(): Single<String> = Single.fromCallable {
        return@fromCallable getImageAsString(currentImage)
    }

    fun getConvertedImage(): Single<String> = Single.fromCallable {
        return@fromCallable getImageAsString(convertedImage)
    }

    /**
     * Выполняет конвертацию и запссь в файл в потоке Schedulers.computation()
     */
    fun convertImage(): Completable = Completable.create { emitter ->
        executeConversion().let {
            if (it) {
                emitter.onComplete()
            } else
                emitter.onError(Exception("Error while converting"))
        }
    }
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())


    /**
     * Непосредственно создет файл с конвертируемого изображения
     * @param name имя файла
     */
    private fun createImageFile(name: String): Boolean {
        if (name == "") {
            throw Exception("Enter Image name")
        }

        currentImage = File(IMAGE_PATH, "$name.jpg")

        return currentImage!!.exists()
    }


    /**
     * Возвращает Uri адрес изображения в виде String
     * @param imageFile файл изображения
     */
    private fun getImageAsString(imageFile: File?): String {
        if (imageFile != null && imageFile.exists()) {
            return imageFile.toUri().toString()
        }
        throw Exception("Image not exists")
    }


    /**
     * Непосредственно выполняет конвертацию из апись в файл
     */
    private fun executeConversion(): Boolean {
        if (currentImage == null) {
            throw Exception("No image selected")
        }
        convertedImage = File(IMAGE_PATH, "$CONVERTED_IMAGE_NAME.png")
        val bmp = BitmapFactory.decodeFile(currentImage?.absolutePath)
        val outStream = FileOutputStream(convertedImage)
        val success = bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream)
        outStream.flush()
        outStream.close()
        return success
    }
}