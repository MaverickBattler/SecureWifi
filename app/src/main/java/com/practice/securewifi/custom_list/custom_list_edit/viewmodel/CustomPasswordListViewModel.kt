package com.practice.securewifi.custom_list.custom_list_edit.viewmodel

import androidx.lifecycle.ViewModel
import com.practice.securewifi.custom_list.custom_list_edit.interactor.DynamicPasswordsInfoInteractor
import com.practice.securewifi.custom_list.custom_list_edit.interactor.FixedPasswordsListInteractor
import com.practice.securewifi.custom_list.interactor.CustomPasswordListsInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CustomPasswordListViewModel(
    private val listName: String,
    private val fixedPasswordsListInteractor: FixedPasswordsListInteractor,
    private val dynamicPasswordsInfoInteractor: DynamicPasswordsInfoInteractor,
    private val customPasswordListsInteractor: CustomPasswordListsInteractor,
) : ViewModel() {

    suspend fun onSaveList(newListName: String): SaveResult {
        return withContext(Dispatchers.IO) {
            val allPasswordListsNames = customPasswordListsInteractor.getPasswordLists()
            if (newListName != listName && allPasswordListsNames.contains(newListName)) {
                SaveResult.LIST_NAME_ALREADY_EXIST
            } else if (newListName.isEmpty()) {
                SaveResult.NO_LIST_NAME_PROVIDED
            } else {
                val fixedPasswordsList = fixedPasswordsListInteractor.passwordList.value
                val personInfoList = dynamicPasswordsInfoInteractor.personInfoList.value
                val placesNamesList = dynamicPasswordsInfoInteractor.placesNamesList.value
                val amountOfGeneratedPasswords =
                    dynamicPasswordsInfoInteractor.amountOfGeneratedPasswords.value
                fixedPasswordsList.let {
                    customPasswordListsInteractor.saveUserList(
                        listName,
                        newListName,
                        fixedPasswordsList,
                        personInfoList,
                        placesNamesList,
                        amountOfGeneratedPasswords
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