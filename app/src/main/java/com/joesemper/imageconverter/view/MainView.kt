package com.joesemper.imageconverter.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView : MvpView {
    fun init()
    fun setImage(path: String)
    fun showResult(text: String?)
    fun setConvertedImage(path: String)
}