package com.practice.securewifi.check_results.interactor

import com.practice.securewifi.check_results.model.DisplayWifiCheckResult
import com.practice.securewifi.data.repository.TriedPasswordsRepository
import com.practice.securewifi.data.repository.WifiCheckResultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class CheckResultsInteractor(
    private val wifiCheckResultRepository: WifiCheckResultRepository,
    private val triedPasswordsRepository: TriedPasswordsRepository
) {

    fun getDisplayWifiCheckResults(): Flow<List<DisplayWifiCheckResult>> {
        val wifiCheckResultsFlow = wifiCheckResultRepository.getAllWifiCheckResultsAsFlow()
        val triedPasswordCountsFlow = triedPasswordsRepository.getTriedPasswordsCountAsFlow()
        return wifiCheckResultsFlow.combine(triedPasswordCountsFlow) { wifiCheckResults, _ ->
            wifiCheckResults.map { wifiCheckResult ->
                DisplayWifiCheckResult(
                    wifiCheckResult.ssid,
                    wifiCheckResult.correctPassword,
                    triedPasswordsRepository.getTriedPasswordsCountForWifi(wifiCheckResult.ssid)
                )
            }
        }
    }
}