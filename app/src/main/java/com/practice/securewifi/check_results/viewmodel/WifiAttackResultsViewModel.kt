package com.practice.securewifi.check_results.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.practice.securewifi.check_results.interactor.TriedPasswordsInteractor

class WifiAttackResultsViewModel(
    wifiSsid: String,
    triedPasswordsInteractor: TriedPasswordsInteractor
) : ViewModel() {

    val triedPasswordList: LiveData<List<String>> =
        triedPasswordsInteractor.getTriedPasswordsForWifiAsFlow(wifiSsid).asLiveData()

    val correctPassword: LiveData<String?> =
        triedPasswordsInteractor.getCorrectPasswordAsFlow(wifiSsid).asLiveData()
}