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

    init {
        prepareForScanningWifies()
    }

    /**
     * @return true Если сканирование было начато успешно,
     * false если неуспешно
     */
    fun startScan() {
        val success = wifiManager.startScan()
        if (!success) {
            Timber.tag(TAG).e("Couldn't start the scan")
            onStartScanFailure()
        }
    }

    fun stop() {
        context.unregisterReceiver(wifiScanReceiver)
    }

    abstract fun onScanSuccess(scanResults: List<ScanResult>)

    abstract fun onScanFailure(oldScanResults: List<ScanResult>)

    abstract fun onStartScanFailure()

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