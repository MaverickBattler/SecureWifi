package com.practice.securewifi.custom_list.custom_list_edit.ui.fragment

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

abstract class BaseViewPagerFragment: Fragment() {

    fun fadeViewOut(view: View) {
        view.isVisible = true
        val fadeOutAnimation = AlphaAnimation(1f, 0f)
        fadeOutAnimation.duration = 100
        fadeOutAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                view.isVisible = false
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
        view.startAnimation(fadeOutAnimation)
    }

    fun makeViewAppear(view: View) {
        view.alpha = 1f
        view.isVisible = true
    }
}