package com.practice.securewifi.check.wifi_points_selection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.practice.securewifi.check.wifi_points_selection.model.WifiPointItem
import com.practice.securewifi.databinding.WifiListChoosingItemBinding
import com.practice.securewifi.scan.util.WifiSignalLevels

class WifiesSelectionAdapter(private val onItemClickListener: (WifiPointItem) -> Unit) :
    ListAdapter<WifiPointItem, WifiesSelectionAdapter.WifiesSelectionViewHolder>(
        WifiesSelectionDiffItemCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WifiesSelectionViewHolder =
        WifiesSelectionViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: WifiesSelectionViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemClickListener)
    }

    class WifiesSelectionViewHolder(private val binding: WifiListChoosingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): WifiesSelectionViewHolder {
                return WifiesSelectionViewHolder(
                    WifiListChoosingItemBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
        }

        fun bind(item: WifiPointItem, onItemClickListener: ((WifiPointItem) -> Unit)) {
            binding.root.setOnClickListener {
                onItemClickListener(item)
            }
            binding.wifiSignalLevel.setImageResource(
                WifiSignalLevels.getImageResourceForSignalLevel(
                    item.signalLevel
                )
            )
            binding.wifi.text = item.ssid
            binding.wifiAddCheckbox.isChecked = item.selected
        }
    }
}