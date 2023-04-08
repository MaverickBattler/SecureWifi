package com.practice.securewifi

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat

class ScanFragment : Fragment() {

    private lateinit var wifiScanReceiver: BroadcastReceiver
    private lateinit var wifiManager: WifiManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_scan, container, false)

        wifiManager = WifiManagerProvider.getWifiManager(requireActivity())

        prepareForScanningWifies()

        val buttonScan: Button = view.findViewById(R.id.start_scan_button)
        val scanResultsTextView: TextView = view.findViewById(R.id.scan_results_textview)

        buttonScan.setOnClickListener {
            scanResultsTextView.text = getString(R.string.starting_scan)
            println("Starting scan...")
            val success = wifiManager.startScan()
            if (!success) {
                // scan failure handling
                println("Scan failed...")
                scanFailure()
            }
        }

        return view
    }

    private fun prepareForScanningWifies() {
        wifiScanReceiver = object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {
                val success = intent.getBooleanExtra(
                    WifiManager.EXTRA_RESULTS_UPDATED,
                    false
                )
                if (success) {
                    println("Scan success!")
                    scanSuccess()
                } else {
                    println("Scan failure!")
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
                requireActivity().applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            //Ask for the permission
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
        }
        val results = wifiManager.scanResults
        var stringResult = "Scan success!\n"
        println(results)
        results.forEach {
            stringResult += "BSSID: "
            stringResult += it.BSSID + "\n"
            stringResult += "SSID: "
            stringResult += it.SSID + "\n"
            stringResult += "capabilities: "
            stringResult += it.capabilities + "\n"
        }
        val textView = requireView().findViewById<TextView>(R.id.scan_results_textview)
        textView.text = stringResult
    }

    private fun scanFailure() {
        val textView = requireView().findViewById<TextView>(R.id.scan_results_textview)
        textView.text = getString(R.string.scan_failure)
        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().unregisterReceiver(wifiScanReceiver)
    }
}