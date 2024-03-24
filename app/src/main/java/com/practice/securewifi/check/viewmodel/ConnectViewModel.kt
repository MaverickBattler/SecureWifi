package com.practice.securewifi.check.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ConnectViewModel(
    private val application: Application,
    selectedWifiesPreviewInteractor: SelectedWifiesPreviewInteractor,
    selectedPasswordListsPreviewInteractor: SelectedPasswordListsPreviewInteractor
) : ViewModel(), UpdateListener {

    val selectedWifiesPreviewUiState: Flow<SelectedWifiesPreviewUiState> =
        selectedWifiesPreviewInteractor.getSelectedWifiesPreview()

    val selectedPasswordListsPreviewUiState: Flow<SelectedPasswordListsPreviewUiState> =
        selectedPasswordListsPreviewInteractor.getSelectedPasswordListsPreview()

    private val _securityCheckButtonState: MutableStateFlow<SecurityCheckButton.State> =
        MutableStateFlow(SecurityCheckButton.State.INITIAL)
    val securityCheckButtonState = _securityCheckButtonState.asStateFlow()

    private val _attackInfoText: MutableStateFlow<String> = MutableStateFlow("")
    val attackInfoText = _attackInfoText.asStateFlow()

    fun onRetrieveLatestDataFromService(latestData: Pair<Command, Command?>) {
        onUpdate(latestData.first)
        latestData.second?.let { secondCommand ->
            onUpdate(secondCommand)
        }
    }

    fun onRebindToService() {
        viewModelScope.launch(Dispatchers.Main) {
            _securityCheckButtonState.emit(SecurityCheckButton.State.PREPARATION)
        }
    }

    fun onSetInitialState() {
        viewModelScope.launch(Dispatchers.Main) {
            _securityCheckButtonState.emit(SecurityCheckButton.State.INITIAL)
        }
    }

    fun onStartCheck() {
        viewModelScope.launch(Dispatchers.Main) {
            _securityCheckButtonState.emit(SecurityCheckButton.State.PREPARATION)
        }
    }

    fun onBindToService(successfully: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            if (successfully) {
                _securityCheckButtonState.emit(SecurityCheckButton.State.PROGRESS)
            } else {
                _attackInfoText.emit(application.getString(R.string.connection_start_failure))
                _securityCheckButtonState.emit(SecurityCheckButton.State.INITIAL)
            }
        }
    }

    override fun onUpdate(command: Command) {
        viewModelScope.launch(Dispatchers.Main) {
            when (command) {
                is Command.StopConnections -> {
                    _securityCheckButtonState.emit(SecurityCheckButton.State.INITIAL)
                }

                is Command.PrepareForConnections -> {
                    _securityCheckButtonState.emit(SecurityCheckButton.State.PREPARATION)
                }

                is Command.StartConnections -> {
                    _securityCheckButtonState.emit(SecurityCheckButton.State.PROGRESS)
                }

                is Command.ShowMessageToUser -> {
                    _attackInfoText.emit(command.message)
                }
            }
        }
    }
}