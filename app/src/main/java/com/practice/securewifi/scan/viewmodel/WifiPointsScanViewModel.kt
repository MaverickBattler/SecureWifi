package com.practice.securewifi.scan.viewmodel

import android.app.Application
import android.net.wifi.ScanResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.securewifi.scan.interactor.ScanResultsInteractor
import com.practice.securewifi.scan.mapper.WifiScanResultsMapper
import com.practice.securewifi.scan.model.ScanResultInfo
import com.practice.securewifi.scan.repository.WifiInfoRepository
import com.practice.securewifi.scan_feature.WifiScanManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class WifiPointsScanViewModel(
    private val application: Application,
    private val wifiScanResultsMapper: WifiScanResultsMapper,
    private val wifiInfoRepository: WifiInfoRepository,
    private val scanResultsInteractor: ScanResultsInteractor
): ViewModel() {

    private val _scanResultInfo: MutableLiveData<ScanResultInfo> = MutableLiveData(ScanResultInfo.Loading)
    val scanResultInfo: LiveData<ScanResultInfo> = _scanResultInfo

    private val _openWifiInfoEvent: MutableSharedFlow<Unit> = MutableSharedFlow()
    val openWifiInfoEvent: SharedFlow<Unit> = _openWifiInfoEvent.asSharedFlow()

    private var wifiScanManager: WifiScanManager = object: WifiScanManager(application) {
        override fun onScanSuccess(scanResults: List<ScanResult>) {
            scanSuccess(scanResults)
        }

        override fun onScanFailure(oldScanResults: List<ScanResult>) {
            scanFailure(oldScanResults)
        }
    }

    fun onWifiItemClick(wifiSsid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val scanResult = scanResultsInteractor.getLatestScanResults().find { it.SSID == wifiSsid }
            scanResult?.let {
                wifiInfoRepository.setSelectedWifiInfo(scanResult)
                _openWifiInfoEvent.emit(Unit)
            }
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
        scanResultsInteractor.updateScanResults(scanResults)
        val wifiScanInfo = wifiScanResultsMapper.mapScanResultInfo(scanResults, true)
        _scanResultInfo.postValue(wifiScanInfo)
    }

    private fun scanFailure(oldScanResults: List<ScanResult>) {
        val wifiScanInfo = wifiScanResultsMapper.mapScanResultInfo(oldScanResults, false)
        _scanResultInfo.postValue(wifiScanInfo)
    }
}