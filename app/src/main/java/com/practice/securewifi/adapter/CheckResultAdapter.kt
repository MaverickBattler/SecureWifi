package com.practice.securewifi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.practice.securewifi.R
import com.practice.securewifi.databinding.WifiCheckResultItemBinding
import com.practice.securewifi.diffutil.CheckResultDiffItemCallback
import com.practice.securewifi.domain.display.DisplayWifiCheckResult

class CheckResultAdapter :
    ListAdapter<DisplayWifiCheckResult, CheckResultAdapter.CheckResultViewHolder>(
        CheckResultDiffItemCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckResultViewHolder =
        CheckResultViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: CheckResultViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class CheckResultViewHolder(private val binding: WifiCheckResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): CheckResultViewHolder {
                return CheckResultViewHolder(
                    WifiCheckResultItemBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
        }

        fun bind(item: DisplayWifiCheckResult) {
            val wifiSsidTextView = binding.wifiSsidTextview
            val passwordGuessedTextView = binding.passwordGuessedTextview
            val passwordCheckCountTextView = binding.passwordsCheckedCountTextview
            wifiSsidTextView.text = item.ssid
            val textViewContext = passwordGuessedTextView.context
            if (item.correctPassword != null) {
                passwordGuessedTextView.text =
                    textViewContext.getString(R.string.password_was_hacked)
                wifiSsidTextView.setTextColor(
                    ContextCompat.getColor(
                        textViewContext, R.color.success_green
                    )
                )
            } else {
                passwordGuessedTextView.text =
                    textViewContext.getString(R.string.password_was_not_hacked)
                wifiSsidTextView.setTextColor(
                    ContextCompat.getColor(
                        textViewContext, R.color.dark_gray
                    )
                )
            }
            passwordCheckCountTextView.text = item.triedPasswordsCount.toString()
        }
    }
}