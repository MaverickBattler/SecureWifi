package com.practice.securewifi.app.core.base

import android.content.res.Resources
import android.graphics.Rect
import androidx.annotation.CallSuper
import androidx.fragment.app.DialogFragment

abstract class BaseDialogFragment: DialogFragment() {

    /** Ширина диалогового окна в процентах. 100F - ширина по умолчанию*/
    open val dialogWidth: Float = 100F

    /** Высота диалогового окна в процентах. 100F - высота по умолчанию */
    open val dialogHeight: Float = 100F

    @CallSuper
    override fun onStart() {
        super.onStart()
        setWidthAndHeightPercent(dialogWidth, dialogHeight)
    }

    private fun setWidthAndHeightPercent(percentageWidth: Float, percentageHeight: Float) {
        val percentWidth = percentageWidth / 100
        val percentHeight = percentageHeight / 100
        val dm = Resources.getSystem().displayMetrics
        val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
        val percentWidthResult = rect.width() * percentWidth
        val percentHeightResult = rect.height() * percentHeight
        dialog?.window?.setLayout(percentWidthResult.toInt(), percentHeightResult.toInt())
    }
}