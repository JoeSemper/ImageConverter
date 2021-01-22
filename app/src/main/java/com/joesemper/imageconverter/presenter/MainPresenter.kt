package com.joesemper.imageconverter.presenter

import com.joesemper.imageconverter.model.ImageManager
import com.joesemper.imageconverter.view.MainView
import moxy.MvpPresenter

class MainPresenter : MvpPresenter<MainView>() {

    private var imageManager = ImageManager()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    /**
     * При нажати на кнопку "FIND" во View находит картину с указанным именем,
     * если такой нет бросает Exception
     *
     * @param imageName - имя файла, введенное пользователем
     */
    fun onFindClicked(imageName: String) {
        imageManager.findImage(imageName).subscribe({
            setImageOnView()
        }, {
            viewState.showResult("${it.message}")
        })
    }

    /**
     * При нажатии на кнопку "CONVERT" во View начинает конвертацию и запсь в файл,
     * если конвертация успешна передает во View, адрес конвертированного изображения
     */
    fun onConvertClicked() {
        imageManager.convertImage().subscribe({
            onImageConverted()
        } , {
            viewState.showResult(it.message)
        })
    }

    /**
     * Передает во View адрес изображения для отображения если такая есть
     */
    private fun setImageOnView() {
        imageManager.getCurrentImage().subscribe({s ->
            viewState.setImage(s)
            viewState.showResult("Image found")
        }, {
            viewState.showResult("${it.message}")
        })
    }

    /**
     * Передает во View адрес отконвертированного изображения для отображения и
     * результат конвертации
     */
    private fun onImageConverted() {
        imageManager.getConvertedImage().subscribe(){path ->
            viewState.setConvertedImage(path)
            viewState.showResult("Conversion complete")
        }
    }

}