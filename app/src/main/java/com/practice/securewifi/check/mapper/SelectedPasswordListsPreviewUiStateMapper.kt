package com.practice.securewifi.check.mapper

import android.app.Application
import com.practice.securewifi.R
import com.practice.securewifi.check.model.SelectedPasswordListsPreviewUiState
import com.practice.securewifi.data.entity.PasswordList

class SelectedPasswordListsPreviewUiStateMapper(
    private val application: Application
) {

    fun map(passwordLists: List<PasswordList>): SelectedPasswordListsPreviewUiState {
        val selectedPasswordListsNames = getSelectedPasswordListsNames(passwordLists)
        return when {
            selectedPasswordListsNames.isEmpty() -> {
                SelectedPasswordListsPreviewUiState(
                    application.getString(R.string.no_password_lists_selected),
                    R.attr.textColorInactive
                )
            }

            selectedPasswordListsNames.size == 1 -> {
                SelectedPasswordListsPreviewUiState(
                    selectedPasswordListsNames[0],
                    R.attr.textColorMain
                )
            }

            selectedPasswordListsNames.size == 2 -> {
                SelectedPasswordListsPreviewUiState(
                    selectedPasswordListsNames[0] + ", " + selectedPasswordListsNames[1],
                    R.attr.textColorMain
                )
            }

            else -> {
                SelectedPasswordListsPreviewUiState(
                    application.getString(
                        R.string.something_and_n_others,
                        selectedPasswordListsNames[0],
                        selectedPasswordListsNames.size - 1
                    ),
                    R.attr.textColorMain
                )
            }
        }
    }

    private fun getSelectedPasswordListsNames(passwordLists: List<PasswordList>): List<String> {
        return passwordLists.filter { passwordList ->
            passwordList.selected
        }.map { passwordList ->
            passwordList.listName
        }
    }
}