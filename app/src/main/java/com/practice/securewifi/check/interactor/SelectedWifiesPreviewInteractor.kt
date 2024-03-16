package com.practice.securewifi.check.interactor

import com.practice.securewifi.check.mapper.SelectedWifiesPreviewUiStateMapper
import com.practice.securewifi.check.model.SelectedWifiesPreviewUiState
import com.practice.securewifi.data.repository.SelectedWifiesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SelectedWifiesPreviewInteractor(
    private val selectedWifiesRepository: SelectedWifiesRepository,
    private val selectedWifiesPreviewUiStateMapper: SelectedWifiesPreviewUiStateMapper
) {

    fun getSelectedWifiesPreview(): Flow<SelectedWifiesPreviewUiState> {
        return selectedWifiesRepository.getSelectedWifiesListAsFlow().map { selectedWifies ->
            selectedWifiesPreviewUiStateMapper.map(selectedWifies)
        }
    }
}