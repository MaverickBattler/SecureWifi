package com.practice.securewifi.scan.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.practice.securewifi.databinding.WifiScanResultItemBinding
import com.practice.securewifi.scan.model.WifiScanResult
import com.practice.securewifi.scan.util.WifiSignalLevels

class ScanResultAdapter :
    ListAdapter<WifiScanResult, ScanResultAdapter.ScanResultViewHolder>(ScanResultDiffItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanResultViewHolder =
        ScanResultViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: ScanResultViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ScanResultViewHolder(private val binding: WifiScanResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): ScanResultViewHolder {
                return ScanResultViewHolder(
                    WifiScanResultItemBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
        }

        fun bind(item: WifiScanResult) {
            binding.wifiSsidTextview.text = item.ssid
            binding.wifiCapabilitiesTextview.text = item.capabilities
            binding.wifiSignalLevel.setImageResource(
                WifiSignalLevels.getImageResourceForSignalLevel(
                    item.signalLevel
                )
            )
        }
    }
}