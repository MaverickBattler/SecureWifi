package com.practice.securewifi.check.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.practice.securewifi.check.interactor.SelectedPasswordListsPreviewInteractor
import com.practice.securewifi.check.interactor.SelectedWifiesPreviewInteractor
import com.practice.securewifi.check.model.SelectedPasswordListsPreviewUiState
import com.practice.securewifi.check.model.SelectedWifiesPreviewUiState

class ConnectViewModel(
    selectedWifiesPreviewInteractor: SelectedWifiesPreviewInteractor,
    selectedPasswordListsPreviewInteractor: SelectedPasswordListsPreviewInteractor
) : ViewModel() {

    val selectedWifiesPreviewUiState: LiveData<SelectedWifiesPreviewUiState> =
        selectedWifiesPreviewInteractor.getSelectedWifiesPreview().asLiveData()

    val selectedPasswordListsPreviewUiState: LiveData<SelectedPasswordListsPreviewUiState> =
        selectedPasswordListsPreviewInteractor.getSelectedPasswordListsPreview().asLiveData()
}