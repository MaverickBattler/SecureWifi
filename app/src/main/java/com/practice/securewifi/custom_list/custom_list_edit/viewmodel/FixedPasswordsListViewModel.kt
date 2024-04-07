package com.practice.securewifi.custom_list.custom_list_edit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.securewifi.custom_list.custom_list_edit.interactor.FixedPasswordsListInteractor
import com.practice.securewifi.custom_list.custom_list_edit.interactor.GetFixedPasswordsForListInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FixedPasswordsListViewModel(
    private val listName: String,
    private val fixedPasswordsListInteractor: FixedPasswordsListInteractor,
    private val getFixedPasswordsForListInteractor: GetFixedPasswordsForListInteractor
): ViewModel() {

    val fixedPasswordsList: StateFlow<List<String>> = fixedPasswordsListInteractor.passwordList

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val storedPasswordList = getFixedPasswordsForListInteractor.getPasswordsForList(listName)
            fixedPasswordsListInteractor.updatePasswordList(storedPasswordList)
        }
    }

    fun onDeletePasswordFromList(password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            fixedPasswordsListInteractor.deletePasswordFromList(password)
        }
    }

    fun onAddNewPasswordToList(password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            fixedPasswordsListInteractor.insertPasswordToList(password)
        }
    }
}