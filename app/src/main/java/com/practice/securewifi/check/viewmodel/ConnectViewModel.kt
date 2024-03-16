package com.practice.securewifi.check.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.practice.securewifi.R
import com.practice.securewifi.check.Command
import com.practice.securewifi.check.UpdateListener
import com.practice.securewifi.check.interactor.SelectedPasswordListsPreviewInteractor
import com.practice.securewifi.check.interactor.SelectedWifiesPreviewInteractor
import com.practice.securewifi.check.model.SelectedPasswordListsPreviewUiState
import com.practice.securewifi.check.model.SelectedWifiesPreviewUiState
import com.practice.securewifi.check.ui.SecurityCheckButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConnectViewModel(
    private val application: Application,
    selectedWifiesPreviewInteractor: SelectedWifiesPreviewInteractor,
    selectedPasswordListsPreviewInteractor: SelectedPasswordListsPreviewInteractor
) : ViewModel(), UpdateListener {

    val selectedWifiesPreviewUiState: LiveData<SelectedWifiesPreviewUiState> =
        selectedWifiesPreviewInteractor.getSelectedWifiesPreview().asLiveData()

    val selectedPasswordListsPreviewUiState: LiveData<SelectedPasswordListsPreviewUiState> =
        selectedPasswordListsPreviewInteractor.getSelectedPasswordListsPreview().asLiveData()


    private val _securityCheckButtonState: MutableLiveData<SecurityCheckButton.State> =
        MutableLiveData(SecurityCheckButton.State.INITIAL)
    val securityCheckButtonState: LiveData<SecurityCheckButton.State>
        get() = _securityCheckButtonState

    private val _attackInfoText: MutableLiveData<String> = MutableLiveData()
    val attackInfoText: LiveData<String>
        get() = _attackInfoText

    fun onRetrieveLatestDataFromService(latestData: Pair<Command, Command?>) {
        onUpdate(latestData.first)
        latestData.second?.let { secondCommand ->
            onUpdate(secondCommand)
        }
    }

    fun onRebindToService() {
        _securityCheckButtonState.postValue(SecurityCheckButton.State.PREPARATION)
    }

    fun onSetInitialState() {
        _securityCheckButtonState.postValue(SecurityCheckButton.State.INITIAL)
    }

    fun onStartCheck() {
        _securityCheckButtonState.postValue(SecurityCheckButton.State.PREPARATION)
    }

    fun onBindToService(successfully: Boolean) {
        if (successfully) {
            _securityCheckButtonState.postValue(SecurityCheckButton.State.PROGRESS)
        } else {
            _attackInfoText.postValue(application.getString(R.string.connection_start_failure))
            _securityCheckButtonState.postValue(SecurityCheckButton.State.INITIAL)
        }
    }

    override fun onUpdate(command: Command) {
        viewModelScope.launch(Dispatchers.Main) {
            when (command) {
                is Command.StopConnections -> {
                    _securityCheckButtonState.postValue(SecurityCheckButton.State.INITIAL)
                }

                is Command.PrepareForConnections -> {
                    _securityCheckButtonState.postValue(SecurityCheckButton.State.PREPARATION)
                }

                is Command.StartConnections -> {
                    _securityCheckButtonState.postValue(SecurityCheckButton.State.PROGRESS)
                }

                is Command.ShowMessageToUser -> {
                    _attackInfoText.postValue(command.message)
                }
            }
        }
    }
}