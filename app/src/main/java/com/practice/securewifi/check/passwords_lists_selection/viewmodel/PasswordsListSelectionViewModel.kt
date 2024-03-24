package com.practice.securewifi.check.passwords_lists_selection.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.securewifi.check.passwords_lists_selection.interactor.PasswordsListsInteractor
import com.practice.securewifi.check.passwords_lists_selection.model.PasswordListModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PasswordsListSelectionViewModel(
    private val passwordsListsInteractor: PasswordsListsInteractor
) : ViewModel() {

    val passwordsLists: Flow<List<PasswordListModel>> = passwordsListsInteractor.passwordModelList

    fun onPasswordListInListClicked(passwordList: PasswordListModel) {
        viewModelScope.launch(Dispatchers.IO) {
            passwordsListsInteractor.switchPasswordListSelection(
                passwordList.listName,
                passwordList.selected
            )
        }
    }

    fun onSelectAllButtonClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            passwordsListsInteractor.switchAllPasswordListSelection()
        }
    }
}