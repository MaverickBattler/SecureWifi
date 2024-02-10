package com.practice.securewifi.scan.ui.fragment

import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.practice.securewifi.R
import com.practice.securewifi.app.core.askForAccessFineLocationPermissionIfNeeded
import com.practice.securewifi.databinding.FragmentScanBinding
import com.practice.securewifi.scan.model.WifiScanResult
import com.practice.securewifi.scan.ui.ScanResultAdapter
import com.practice.securewifi.scan_feature.WifiScanManager

class ScanFragment : Fragment() {

    private lateinit var wifiScanManager: WifiScanManager

    private lateinit var scanResultAdapter: ScanResultAdapter

    private var _binding: FragmentScanBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)

        binding.startScanButton.setOnClickListener {
            askForAccessFineLocationPermissionIfNeeded()
            val scanResultsTextView = binding.scanResultsTextview
            scanResultsTextView.visibility = View.VISIBLE
            scanResultsTextView.text = getString(R.string.starting_scan)
            wifiScanManager.startScan()
        }

        scanResultAdapter = ScanResultAdapter()
        binding.recyclerviewScan.adapter = scanResultAdapter

        wifiScanManager = object: WifiScanManager(requireActivity().applicationContext) {
            override fun onScanSuccess(scanResults: List<ScanResult>) {
                scanSuccess(scanResults)
            }

            override fun onScanFailure(oldScanResults: List<ScanResult>) {
                scanFailure(oldScanResults)
            }

            override fun onStartScanFailure() {
                scanFailure(emptyList())
            }
        }
        return binding.root
    }

    private fun scanSuccess(scanResults: List<ScanResult>) {
        val scanResultsTextView = binding.scanResultsTextview
        scanResultsTextView.visibility = View.GONE
        val displayableResults = scanResultsToDisplayableResults(scanResults)
        scanResultAdapter.submitList(displayableResults)
    }

    private fun scanFailure(oldScanResults: List<ScanResult>) {
        val scanResultsTextView = binding.scanResultsTextview
        if (oldScanResults.isNotEmpty()) {
            scanResultsTextView.visibility = View.GONE
            // handle failure: new scan did NOT succeed
            // showing OLD scan results
            val wifiScanResults = scanResultsToDisplayableResults(oldScanResults)
            scanResultAdapter.submitList(wifiScanResults)
        } else {
            scanResultsTextView.visibility = View.VISIBLE
            scanResultsTextView.text = getString(R.string.scan_failure)
        }
    }

    private fun scanResultsToDisplayableResults(results: List<ScanResult>): List<WifiScanResult> {
        return results.filter { it.SSID != "" }.map { mapToDisplayableResult(it) }
            .sortedByDescending { it.signalLevel }
    }

    private fun mapToDisplayableResult(result: ScanResult): WifiScanResult {
        val signalLevel = WifiManager.calculateSignalLevel(result.level, 6)
        return WifiScanResult(result.SSID, result.capabilities, signalLevel)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        wifiScanManager.stop()
    }
}