package com.practice.securewifi.check.mapper

import android.app.Application
import com.practice.securewifi.R
import com.practice.securewifi.check.model.SelectedWifiesPreviewUiState
import com.practice.securewifi.data.entity.SelectedWifi

class SelectedWifiesPreviewUiStateMapper(
    private val application: Application
) {

    fun map(selectedWifies: List<SelectedWifi>): SelectedWifiesPreviewUiState {
        val selectedWifiesSsids = getSelectedWifiesSsids(selectedWifies)
        return when {
            selectedWifiesSsids.isEmpty() -> {
                SelectedWifiesPreviewUiState(
                    application.getString(R.string.no_wifies_selected),
                    R.attr.textColorInactive
                )
            }

            selectedWifiesSsids.size == 1 -> {
                SelectedWifiesPreviewUiState(
                    selectedWifiesSsids[0],
                    R.attr.textColorMain
                )
            }

            selectedWifiesSsids.size == 2 -> {
                SelectedWifiesPreviewUiState(
                    selectedWifiesSsids[0] + ", " + selectedWifiesSsids[1],
                    R.attr.textColorMain
                )
            }

            else -> {
                SelectedWifiesPreviewUiState(
                    application.getString(
                        R.string.something_and_n_others,
                        selectedWifiesSsids[0],
                        selectedWifiesSsids.size - 1
                    ),
                    R.attr.textColorMain
                )
            }
        }
    }

    private fun getSelectedWifiesSsids(selectedWifies: List<SelectedWifi>): List<String> {
        return selectedWifies.map { it.ssid }
    }
}