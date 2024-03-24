package com.practice.securewifi.scan.wifi_info.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.practice.securewifi.scan.wifi_info.interactor.WifiInfoUiStateInteractor
import com.practice.securewifi.scan.wifi_info.model.WifiInfoUiState

class WifiInfoViewModel(
    wifiInfoUiStateInteractor: WifiInfoUiStateInteractor
) : ViewModel() {

    val wifiInfoUiState: LiveData<WifiInfoUiState?> =
        wifiInfoUiStateInteractor.wifiInfoUiState.asLiveData()

}