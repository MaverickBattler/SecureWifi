package com.practice.securewifi.check.interactor

import com.practice.securewifi.check.mapper.SelectedPasswordListsPreviewUiStateMapper
import com.practice.securewifi.check.model.SelectedPasswordListsPreviewUiState
import com.practice.securewifi.data.repository.PasswordListsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SelectedPasswordListsPreviewInteractor(
    private val passwordListsRepository: PasswordListsRepository,
    private val selectedPasswordListsPreviewUiStateMapper: SelectedPasswordListsPreviewUiStateMapper,
) {

    fun getSelectedPasswordListsPreview(): Flow<SelectedPasswordListsPreviewUiState> {
        return passwordListsRepository.getPasswordListsAsFlow().map { passwordLists ->
            selectedPasswordListsPreviewUiStateMapper.map(passwordLists)
        }
    }
}