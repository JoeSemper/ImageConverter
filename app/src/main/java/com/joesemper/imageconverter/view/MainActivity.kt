package com.joesemper.imageconverter.view

import android.os.Bundle
import android.widget.Toast
import androidx.core.net.toUri
import com.joesemper.imageconverter.R
import com.joesemper.imageconverter.presenter.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter


class MainActivity : MvpAppCompatActivity(), MainView {

    private val presenter: MainPresenter by moxyPresenter { MainPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun init() {
        setOnClickListeners()
    }

    override fun setImage(path: String) {
        imageView.setImageURI(path.toUri())
    }

    override fun setConvertedImage(path: String) {
        converted_image.setImageURI(null)
        converted_image.setImageURI(path.toUri())
    }

    /**
     * Возвращает Uri адрес изображения в виде String
     * @param text текст сообщения
     */
    override fun showResult(text: String?) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun setOnClickListeners() {
        setFindClickListener()
        setConvertClickListener()
    }

    private fun setFindClickListener() {
        button_find.setOnClickListener{
            presenter.onFindClicked(editText.text.toString())
        }
    }

    private fun setConvertClickListener() {
        button_convert.setOnClickListener{
            presenter.onConvertClicked()
        }
    }
}
