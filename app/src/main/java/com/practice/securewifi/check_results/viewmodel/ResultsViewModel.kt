package com.practice.securewifi.check_results.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.practice.securewifi.check_results.interactor.CheckResultsInteractor
import com.practice.securewifi.check_results.model.DisplayWifiCheckResult

class ResultsViewModel(checkResultsInteractor: CheckResultsInteractor): ViewModel() {

    val displayWifiCheckResults: LiveData<List<DisplayWifiCheckResult>> =
        checkResultsInteractor.getDisplayWifiCheckResults().asLiveData()
}