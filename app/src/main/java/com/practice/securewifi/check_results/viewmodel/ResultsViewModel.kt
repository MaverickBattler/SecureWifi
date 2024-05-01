package com.practice.securewifi.check_results.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.securewifi.check_results.interactor.CheckResultsInteractor
import com.practice.securewifi.check_results.interactor.DeleteAllResultsInteractor
import com.practice.securewifi.check_results.model.DisplayWifiCheckResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ResultsViewModel(
    checkResultsInteractor: CheckResultsInteractor,
    private val deleteAllResultsInteractor: DeleteAllResultsInteractor
): ViewModel() {

    val displayWifiCheckResults: Flow<List<DisplayWifiCheckResult>> =
        checkResultsInteractor.getDisplayWifiCheckResults()

    fun deleteAllResults() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteAllResultsInteractor.deleteAllResults()
        }
    }
}