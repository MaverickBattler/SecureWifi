package com.practice.securewifi.adapter

import android.net.wifi.WifiManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.practice.securewifi.R
import com.practice.securewifi.diffutil.ScanResultDiffItemCallback
import com.practice.securewifi.databinding.WifiScanResultItemBinding
import com.practice.securewifi.domain.display.WifiScanResult

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
            binding.wifiSignalLevelTextview.text =
                binding.root.context.getString(R.string.signal_level, WifiManager
                    .calculateSignalLevel(item.signalLevel, 10))
        }
    }
}