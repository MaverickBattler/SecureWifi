package com.practice.securewifi.check_results.viewmodel

import androidx.lifecycle.ViewModel
import com.practice.securewifi.check_results.interactor.CheckResultsInteractor
import com.practice.securewifi.check_results.model.DisplayWifiCheckResult
import kotlinx.coroutines.flow.Flow

class ResultsViewModel(checkResultsInteractor: CheckResultsInteractor): ViewModel() {

    val displayWifiCheckResults: Flow<List<DisplayWifiCheckResult>> =
        checkResultsInteractor.getDisplayWifiCheckResults()
}