package com.practice.securewifi.scan.viewmodel

import android.app.Application
import android.net.wifi.ScanResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practice.securewifi.scan.mapper.WifiScanResultsMapper
import com.practice.securewifi.scan.model.ScanResultInfo
import com.practice.securewifi.scan_feature.WifiScanManager

class WifiPointsScanViewModel(
    private val application: Application,
    private val wifiScanResultsMapper: WifiScanResultsMapper
): ViewModel() {

    private val _scanResultInfo: MutableLiveData<ScanResultInfo> = MutableLiveData(ScanResultInfo.Loading)
    val scanResultInfo: LiveData<ScanResultInfo> = _scanResultInfo

    private var wifiScanManager: WifiScanManager = object: WifiScanManager(application) {
        override fun onScanSuccess(scanResults: List<ScanResult>) {
            scanSuccess(scanResults)
        }

        override fun onScanFailure(oldScanResults: List<ScanResult>) {
            scanFailure(oldScanResults)
        }
    }

    fun onRefresh() {
        wifiScanManager.startScan()
    }

    override fun onCleared() {
        wifiScanManager.stop()
        super.onCleared()
    }

    private fun scanSuccess(scanResults: List<ScanResult>) {
        val wifiScanResults = wifiScanResultsMapper.mapScanResults(scanResults)
        val wifiScanInfo = ScanResultInfo.ScanSuccess(wifiScanResults)
        _scanResultInfo.postValue(wifiScanInfo)
    }

    private fun scanFailure(oldScanResults: List<ScanResult>) {
        val wifiScanResults = wifiScanResultsMapper.mapScanResults(oldScanResults)
        val wifiScanInfo = ScanResultInfo.ScanFailure(wifiScanResults)
        _scanResultInfo.postValue(wifiScanInfo)
    }
}