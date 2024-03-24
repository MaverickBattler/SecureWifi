package com.practice.securewifi.scan.wifi_info.viewmodel

import androidx.lifecycle.ViewModel
import com.practice.securewifi.scan.wifi_info.interactor.WifiInfoUiStateInteractor
import com.practice.securewifi.scan.wifi_info.model.WifiInfoUiState
import kotlinx.coroutines.flow.StateFlow

class WifiInfoViewModel(
    wifiInfoUiStateInteractor: WifiInfoUiStateInteractor
) : ViewModel() {

    val wifiInfoUiState: StateFlow<WifiInfoUiState> = wifiInfoUiStateInteractor.wifiInfoUiState

}