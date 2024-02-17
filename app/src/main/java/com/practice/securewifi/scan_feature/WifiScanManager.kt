package com.practice.securewifi.scan_feature

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import com.practice.securewifi.app.core.util.WifiManagerProvider
import timber.log.Timber

abstract class WifiScanManager(
    private val context: Context
) {

    private lateinit var wifiScanReceiver: BroadcastReceiver

    private val wifiManager = WifiManagerProvider.getWifiManager(context)

    private var receiverWasRegistered = false

    init {
        prepareForScanningWifies()
    }

    fun startScan() {
        wifiManager.startScan()
    }

    fun stop() {
        if (receiverWasRegistered) {
            context.unregisterReceiver(wifiScanReceiver)
            receiverWasRegistered = false
        }
    }

    abstract fun onScanSuccess(scanResults: List<ScanResult>)

    abstract fun onScanFailure(oldScanResults: List<ScanResult>)

    private fun prepareForScanningWifies() {

        wifiScanReceiver = object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {
                val success = intent.getBooleanExtra(
                    WifiManager.EXTRA_RESULTS_UPDATED,
                    false
                )
                if (success) {
                    onScanSuccessLocal()
                } else {
                    onScanFailureLocal()
                }
            }
        }

        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        context.registerReceiver(wifiScanReceiver, intentFilter)
        receiverWasRegistered = true
    }

    private fun onScanSuccessLocal() {
        try {
            Timber.tag(TAG).i("Scan success!")
            onScanSuccess(wifiManager.scanResults)
        } catch (e: SecurityException) {
            Timber.tag(TAG).e(e)
            onScanFailure(emptyList())
        }
    }

    private fun onScanFailureLocal() {
        try {
            Timber.tag(TAG).i("Scan failure!")
            onScanFailure(wifiManager.scanResults)
        } catch (e: SecurityException) {
            Timber.tag(TAG).e(e)
            onScanFailure(emptyList())
        }
    }

    private companion object {
        val TAG: String = WifiScanManager::class.java.simpleName
    }
}