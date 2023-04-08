package com.practice.securewifi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class WifiScanManager {
    private lateinit var wifiScanReceiver: BroadcastReceiver

    private val _scanResultsReady = MutableLiveData<Boolean>()

    // Результат выполнения запроса
    val scanResultsReady: LiveData<Boolean>
        get() = _scanResultsReady

    private fun prepareForScanningWifies(activityContext: Context) {

        _scanResultsReady.value = false

        wifiScanReceiver = object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {
                val success = intent.getBooleanExtra(
                    WifiManager.EXTRA_RESULTS_UPDATED,
                    false
                )
                if (success) {
                    println("Scan success!")
                    _scanResultsReady.value = true
                } else {
                    println("Scan failure!")
                    _scanResultsReady.value = false
                }
            }
        }

        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        activityContext.registerReceiver(wifiScanReceiver, intentFilter)
    }

    private fun unregisterReceiver(activityContext: Context) {
        activityContext.unregisterReceiver(wifiScanReceiver)
    }
}