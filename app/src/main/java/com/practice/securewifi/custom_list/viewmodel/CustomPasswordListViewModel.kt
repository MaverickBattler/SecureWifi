package com.practice.securewifi.custom_list.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.practice.securewifi.R
import com.practice.securewifi.data.interactor.CustomPasswordListInteractor
import com.practice.securewifi.data.interactor.CustomPasswordListsInteractor
import com.practice.securewifi.data.interactor.FixedPasswordListsInteractor
import com.practice.securewifi.data.repository.FixedPasswordListsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CustomPasswordListViewModel(
    private val listName: String,
    private val customPasswordListInteractor: CustomPasswordListInteractor,
    private val customPasswordListsInteractor: CustomPasswordListsInteractor,
    private val fixedPasswordListsInteractor: FixedPasswordListsInteractor,
    private val application: Application
) : ViewModel() {

    val customPasswordList: LiveData<List<String>> =
        customPasswordListInteractor.passwordList.asLiveData()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val storedPasswordList = when (listName) {
                application.getString(R.string.adaptive) -> {
                    fixedPasswordListsInteractor.getFixedPasswordListForShow(
                        FixedPasswordListsRepository.FixedPassword.ADAPTIVE,
                    ) + application.getString(R.string.plus_adaptive_passwords)
                }
                application.getString(R.string.most_popular) -> {
                    fixedPasswordListsInteractor.getFixedPasswordListForShow(
                        FixedPasswordListsRepository.FixedPassword.MOST_POPULAR,
                    )
                }
                else -> {
                    customPasswordListInteractor.getPasswordsForList(listName)
                }
            }
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
            val allPasswordListsNames =
                customPasswordListsInteractor.getPasswordLists() + fixedPasswordListsInteractor.getFixedPasswordListsNames()
            if (newListName != listName && allPasswordListsNames.contains(newListName)) {
                SaveResult.LIST_NAME_ALREADY_EXIST
            } else if (newListName.isEmpty()) {
                SaveResult.NO_LIST_NAME_PROVIDED
            } else {
                val customPasswordList = customPasswordList.value
                customPasswordList?.let {
                    customPasswordListsInteractor.saveList(
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