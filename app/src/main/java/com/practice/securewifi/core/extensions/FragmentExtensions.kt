package com.practice.securewifi.core.extensions

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment

fun Fragment.setViewsThatHideKeyboardOnClick(views: List<View>) {
    views.forEach {
        it.setOnClickListener {
            hideKeyboardIfOpened()
        }
    }
}

fun Fragment.hideKeyboardIfOpened() {
    val v = requireActivity().currentFocus
    if (v is EditText) {
        v.clearFocus()
        val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
    }
}