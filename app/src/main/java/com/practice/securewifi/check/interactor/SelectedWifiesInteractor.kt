package com.practice.securewifi.check.interactor

import com.practice.securewifi.data.repository.SelectedWifiesRepository

class SelectedWifiesInteractor(
    private val selectedWifiesRepository: SelectedWifiesRepository
) {

    suspend fun getSelectedWifiesSsids(): List<String> {
        val selectedWifies = selectedWifiesRepository.getSelectedWifiesList()
        return selectedWifies.map { it.ssid }
    }
}