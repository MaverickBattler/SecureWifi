package com.practice.securewifi.core.util

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

object Colors {

    fun getThemeColorFromAttr(attribute: Int, context: Context): Int? {
        val tV = TypedValue()
        val theme: Resources.Theme = context.theme
        val success = theme.resolveAttribute(attribute, tV, true)
        return if (success) {
            tV.data
        } else {
            null
        }
    }
}