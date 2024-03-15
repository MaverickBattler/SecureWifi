package com.practice.securewifi.check.wifi_points_selection.viewmodel

import android.app.Application
import android.net.wifi.ScanResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.securewifi.check.wifi_points_selection.interactor.SelectedWifiesListInteractor
import com.practice.securewifi.check.wifi_points_selection.interactor.WifiScanResultsListInteractor
import com.practice.securewifi.check.wifi_points_selection.mapper.WifiListStateMapper
import com.practice.securewifi.check.wifi_points_selection.model.WifiListState
import com.practice.securewifi.check.wifi_points_selection.model.WifiPointItem
import com.practice.securewifi.scan_feature.WifiScanManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class WifiPointsSelectionViewModel(
    private val application: Application,
    private val selectedWifiesListInteractor: SelectedWifiesListInteractor,
    private val wifiScanResultsListInteractor: WifiScanResultsListInteractor,
    private val wifiListStateMapper: WifiListStateMapper
) : ViewModel() {

    val wifiList: Flow<WifiListState> = selectedWifiesListInteractor.selectedWifiesNamesList
        .combine(wifiScanResultsListInteractor.scanResults) { selectedWifiesList, scanResults ->
            wifiListStateMapper.map(scanResults, selectedWifiesList)
        }
        .flowOn(Dispatchers.IO)

    private var wifiScanManager: WifiScanManager = object : WifiScanManager(application) {
        override fun onScanSuccess(scanResults: List<ScanResult>) {
            viewModelScope.launch(Dispatchers.IO) {
                wifiScanResultsListInteractor.updateScanResults(scanResults)
            }
        }

        override fun onScanFailure(oldScanResults: List<ScanResult>) {
            viewModelScope.launch(Dispatchers.IO) {
                wifiScanResultsListInteractor.updateScanResults(oldScanResults)
            }
        }
    }

    fun onWifiInListClicked(wifiPointItem: WifiPointItem) {
        viewModelScope.launch(Dispatchers.IO) {
            selectedWifiesListInteractor.switchWifiSelection(
                wifiPointItem.ssid,
                wifiPointItem.selected
            )
        }
    }

    fun onSelectAllButtonClicked(currentList: List<WifiPointItem>) {
        viewModelScope.launch(Dispatchers.IO) {
            selectedWifiesListInteractor.switchAllWifiesSelection(
                currentList
            )
        }
    }

    fun startScan() {
        wifiScanManager.startScan()
    }

    override fun onCleared() {
        wifiScanManager.stop()
        super.onCleared()
    }
}