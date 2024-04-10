package com.practice.securewifi.custom_list.custom_list_edit.ui.input_filter

import android.text.InputFilter
import android.text.Spanned

class MinMaxEditTextInputFilter(private val mMin: Int, private val mMax: Int) : InputFilter {

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int,
    ): CharSequence {
        try {
            val newValueString: String = dest.subSequence(0, dstart).toString() +
                    source.subSequence(start, end).toString() +
                    dest.subSequence(dend, dest.length)
            val newValueInt = newValueString.toInt()
            if (isInRange(mMin, mMax, newValueInt)) return source
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return ""
    }

    private fun isInRange(min: Int, max: Int, value: Int): Boolean {
        return if (max > min) {
            value in min..max
        } else {
            value in max..min
        }
    }
}