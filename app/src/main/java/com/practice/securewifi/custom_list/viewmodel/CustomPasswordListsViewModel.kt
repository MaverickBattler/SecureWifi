package com.practice.securewifi.custom_list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.practice.securewifi.custom_list.interactor.CustomPasswordListsInteractor
import com.practice.securewifi.custom_list.model.CustomPasswordList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomPasswordListsViewModel(
    private val customPasswordListsInteractor: CustomPasswordListsInteractor,
) : ViewModel() {

    val customPasswordLists: LiveData<List<CustomPasswordList>> =
        customPasswordListsInteractor.getCustomPasswordListsAsFlow().asLiveData()

    fun onDeletePasswordList(listName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            customPasswordListsInteractor.deletePasswordList(listName)
        }
    }
}