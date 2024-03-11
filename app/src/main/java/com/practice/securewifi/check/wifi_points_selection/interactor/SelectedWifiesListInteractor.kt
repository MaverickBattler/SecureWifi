package com.practice.securewifi.check.wifi_points_selection.interactor

import com.practice.securewifi.check.wifi_points_selection.model.WifiPointItem
import com.practice.securewifi.data.entity.SelectedWifi
import com.practice.securewifi.data.repository.SelectedWifiesRepository
import kotlinx.coroutines.flow.map

class SelectedWifiesListInteractor(
    private val selectedWifiesRepository: SelectedWifiesRepository,
) {

    val selectedWifiesNamesList = selectedWifiesRepository.getSelectedWifiesListAsFlow()
        .map { selectedWifiesList ->
            selectedWifiesList.map { selectedWifi ->
                selectedWifi.ssid
            }
        }

    suspend fun switchWifiSelection(ssid: String, isSelected: Boolean) {
        if (isSelected) {
            selectedWifiesRepository.deleteSelectedWifi(ssid)
        } else {
            selectedWifiesRepository.insertSelectedWifi(SelectedWifi(ssid))
        }
    }

    suspend fun switchAllWifiesSelection(currentList: List<WifiPointItem>) {
        selectedWifiesRepository.deleteAllSelectedWifies()
        if (currentList.find { !it.selected } != null) { // if current list has at least one not selected item
            val scannedWifiesSetAsSelected = currentList.map { wifiPointItem ->
                SelectedWifi(wifiPointItem.ssid)
            }
            selectedWifiesRepository.insertSelectedWifies(scannedWifiesSetAsSelected)
        }
    }
}