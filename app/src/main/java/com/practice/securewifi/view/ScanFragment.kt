package com.practice.securewifi.view

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.practice.securewifi.R
import com.practice.securewifi.util.WifiManagerProvider
import com.practice.securewifi.adapter.ScanResultAdapter
import com.practice.securewifi.databinding.FragmentScanBinding
import com.practice.securewifi.domain.WifiScanResult

class ScanFragment : Fragment() {

    private lateinit var wifiScanReceiver: BroadcastReceiver

    private lateinit var wifiManager: WifiManager

    private lateinit var scanResultAdapter: ScanResultAdapter

    private var _binding: FragmentScanBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)

        wifiManager = WifiManagerProvider.getWifiManager(requireActivity())

        prepareForScanningWifies()

        binding.startScanButton.setOnClickListener {
            val scanResultsTextView = binding.scanResultsTextview
            scanResultsTextView.visibility = View.VISIBLE
            scanResultsTextView.text = getString(R.string.starting_scan)
            val success = wifiManager.startScan()
            if (!success) {
                scanFailure()
            }
        }

        scanResultAdapter = ScanResultAdapter()
        binding.recyclerviewScan.adapter = scanResultAdapter

        return binding.root
    }

    private fun prepareForScanningWifies() {
        wifiScanReceiver = object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {
                val success = intent.getBooleanExtra(
                    WifiManager.EXTRA_RESULTS_UPDATED, false
                )
                if (success) {
                    scanSuccess()
                } else {
                    scanFailure()
                }
            }
        }

        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        requireActivity().registerReceiver(wifiScanReceiver, intentFilter)
    }

    private fun scanSuccess() {
        //Check if ACCESS_FINE_LOCATION permission is granted
        if (ContextCompat.checkSelfPermission(
                requireActivity().applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            //Ask for the permission
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
        }
        val results = wifiManager.scanResults
        val scanResultsTextView = binding.scanResultsTextview
        scanResultsTextView.visibility = View.GONE
        val wifiScanResults = scanResultsToDisplayableResults(results)
        scanResultAdapter.submitList(wifiScanResults)
    }

    private fun scanFailure() {
        //Check if ACCESS_FINE_LOCATION permission is granted
        if (ContextCompat.checkSelfPermission(
                requireActivity().applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            //Ask for the permission
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
        }
        val scanResultsTextView = binding.scanResultsTextview
        if (wifiManager.scanResults.isNotEmpty()) {
            scanResultsTextView.visibility = View.GONE
            // handle failure: new scan did NOT succeed
            // showing OLD scan results
            val results = wifiManager.scanResults
            val wifiScanResults = scanResultsToDisplayableResults(results)
            scanResultAdapter.submitList(wifiScanResults)
        } else {
            scanResultsTextView.visibility = View.VISIBLE
            scanResultsTextView.text = getString(R.string.scan_failure)
        }
    }

    private fun scanResultsToDisplayableResults(results: List<ScanResult>): List<WifiScanResult> =
        results.filter { it.SSID != "" }.map { WifiScanResult(it.SSID, it.capabilities, it.level) }
            .sortedByDescending { it.signalLevel }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().unregisterReceiver(wifiScanReceiver)
    }
}