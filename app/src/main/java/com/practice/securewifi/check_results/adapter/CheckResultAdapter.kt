package com.practice.securewifi.check_results.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.practice.securewifi.R
import com.practice.securewifi.check_results.model.DisplayWifiCheckResult
import com.practice.securewifi.databinding.WifiCheckResultItemBinding
import com.practice.securewifi.core.util.Colors

class CheckResultAdapter(private val onItemClickListener: (String) -> Unit) :
    ListAdapter<DisplayWifiCheckResult, CheckResultAdapter.CheckResultViewHolder>(
        CheckResultDiffItemCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckResultViewHolder =
        CheckResultViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: CheckResultViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemClickListener)
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

        fun bind(item: DisplayWifiCheckResult, onItemClickListener: (String) -> Unit) {
            val wifiSsidTextView = binding.wifiSsidTextview
            val passwordGuessedTextView = binding.passwordGuessedTextview
            val passwordCheckCountTextView = binding.passwordsCheckedCountTextview
            wifiSsidTextView.text = item.ssid
            val context = binding.root.context
            if (item.correctPassword != null) {
                passwordGuessedTextView.text =
                    context.getString(R.string.password_was_hacked)
                wifiSsidTextView.setTextColor(
                    ContextCompat.getColor(
                        context, R.color.success_green
                    )
                )
            } else {
                passwordGuessedTextView.text =
                    context.getString(R.string.password_was_not_hacked)
                val colorFromTheme = Colors.getThemeColorFromAttr(R.attr.textColorMain, context)
                colorFromTheme?.let { color ->
                    wifiSsidTextView.setTextColor(color)
                }
            }
            passwordCheckCountTextView.text = item.triedPasswordsCount.toString()
            binding.root.setOnClickListener {
                onItemClickListener(item.ssid)
            }
        }
    }
}