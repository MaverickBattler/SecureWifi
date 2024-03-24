package com.practice.securewifi.check_results.viewmodel

import androidx.lifecycle.ViewModel
import com.practice.securewifi.check_results.interactor.TriedPasswordsInteractor
import kotlinx.coroutines.flow.Flow

class WifiAttackResultsViewModel(
    wifiSsid: String,
    triedPasswordsInteractor: TriedPasswordsInteractor
) : ViewModel() {

    val triedPasswordList: Flow<List<String>> =
        triedPasswordsInteractor.getTriedPasswordsForWifiAsFlow(wifiSsid)

    val correctPassword: Flow<String?> = triedPasswordsInteractor.getCorrectPasswordAsFlow(wifiSsid)
}