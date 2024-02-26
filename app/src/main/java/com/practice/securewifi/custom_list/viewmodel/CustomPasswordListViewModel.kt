package com.practice.securewifi.custom_list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.practice.securewifi.custom_list.interactor.CustomPasswordListInteractor
import com.practice.securewifi.custom_list.interactor.CustomPasswordListsInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CustomPasswordListViewModel(
    private val listName: String,
    private val customPasswordListInteractor: CustomPasswordListInteractor,
    private val customPasswordListsInteractor: CustomPasswordListsInteractor,
) : ViewModel() {

    val customPasswordList: LiveData<List<String>> =
        customPasswordListInteractor.passwordList.asLiveData()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val storedPasswordList = customPasswordListInteractor.getPasswordsForList(listName)
            customPasswordListInteractor.updatePasswordList(storedPasswordList)
        }
    }

    fun onDeletePasswordFromList(password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            customPasswordListInteractor.deletePasswordFromList(password)
        }
    }

    fun onAddNewPasswordToList(password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            customPasswordListInteractor.insertPasswordToList(password)
        }
    }

    suspend fun onSaveList(newListName: String): SaveResult {
        return withContext(Dispatchers.IO) {
            val allPasswordListsNames = customPasswordListsInteractor.getPasswordLists()
            if (newListName != listName && allPasswordListsNames.contains(newListName)) {
                SaveResult.LIST_NAME_ALREADY_EXIST
            } else if (newListName.isEmpty()) {
                SaveResult.NO_LIST_NAME_PROVIDED
            } else {
                val customPasswordList = customPasswordList.value
                customPasswordList?.let {
                    customPasswordListsInteractor.saveUserList(
                        listName,
                        newListName,
                        customPasswordList
                    )
                }
                SaveResult.SUCCESS
            }
        }
    }

    enum class SaveResult {
        SUCCESS,
        NO_LIST_NAME_PROVIDED,
        LIST_NAME_ALREADY_EXIST
    }
}