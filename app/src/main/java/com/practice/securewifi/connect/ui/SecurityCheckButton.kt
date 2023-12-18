package com.practice.securewifi.connect.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.practice.securewifi.R
import com.practice.securewifi.databinding.SecurityCheckButtonBinding

class SecurityCheckButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding =
        SecurityCheckButtonBinding.inflate(LayoutInflater.from(context), this, true)

    override fun setOnClickListener(listener: OnClickListener?) {
        binding.securityCheckMaterialButton.setOnClickListener(listener)
    }

    fun setState(state: State) {
        when (state) {
            State.INITIAL -> {
                binding.securityCheckMaterialButton.isClickable = true
                setButtonBackgroundImage(R.drawable.play)
                binding.attemptingConnectsProgressbar.visibility = View.GONE
            }
            State.PROGRESS -> {
                binding.securityCheckMaterialButton.isClickable = true
                setButtonBackgroundImage(R.drawable.stop)
                binding.attemptingConnectsProgressbar.visibility = View.VISIBLE
            }
            State.PREPARATION -> {
                binding.securityCheckMaterialButton.isClickable = false
                setButtonBackgroundImage(R.drawable.stop)
                binding.attemptingConnectsProgressbar.visibility = View.VISIBLE
            }
        }
    }

    private fun setButtonBackgroundImage(imgId: Int) {
        val img = ContextCompat.getDrawable(context, imgId)
        binding.securityCheckMaterialButton.setCompoundDrawablesWithIntrinsicBounds(
            img,
            null,
            null,
            null
        )

    }

    enum class State {
        PROGRESS,
        INITIAL,
        PREPARATION
    }
}