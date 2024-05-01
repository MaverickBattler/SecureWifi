package com.practice.securewifi.check_results.interactor

import com.practice.securewifi.data.repository.WifiCheckResultRepository

class DeleteAllResultsInteractor(
    private val wifiCheckResultRepository: WifiCheckResultRepository
) {

    suspend fun deleteAllResults() {
        wifiCheckResultRepository.deleteAllCheckResults()
    }
}