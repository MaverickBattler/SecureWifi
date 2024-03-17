package com.practice.securewifi.scan.wifi_info.viewmodel

import android.net.wifi.ScanResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.practice.securewifi.scan.repository.WifiInfoRepository

class WifiInfoViewModel(
    wifiInfoRepository: WifiInfoRepository
): ViewModel() {

    val wifiInfo: LiveData<ScanResult?> = wifiInfoRepository.wifiInfo.asLiveData()

}