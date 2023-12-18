package com.practice.securewifi.custom_list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.securewifi.data.interactor.CustomPasswordListsInteractor
import com.practice.securewifi.data.interactor.FixedPasswordListsInteractor
import com.practice.securewifi.custom_list.model.CustomPasswordList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CustomPasswordListsViewModel(
    private val customPasswordListsInteractor: CustomPasswordListsInteractor,
    private val fixedPasswordListsInteractor: FixedPasswordListsInteractor
) : ViewModel() {

    private val customPasswordLists: Flow<List<String>> =
        customPasswordListsInteractor.getPasswordListsAsFlow()

    private val _allPasswordLists: MutableLiveData<List<CustomPasswordList>> =
        MutableLiveData(listOf())
    val allPasswordLists: LiveData<List<CustomPasswordList>> = _allPasswordLists

    init {
        viewModelScope.launch(Dispatchers.IO) {
            customPasswordLists.collect { customPasswordLists ->
                _allPasswordLists.postValue(
                    fixedPasswordListsInteractor.getFixedPasswordListsNames()
                        .map { CustomPasswordList(it, false) }
                            + customPasswordLists.map { CustomPasswordList(it, true) }
                )
            }
        }
    }

    fun onDeletePasswordList(listName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            customPasswordListsInteractor.deletePasswordList(listName)
        }
    }

    fun isChosenPasswordListEditable(listName: String): Boolean {
        return !fixedPasswordListsInteractor.getFixedPasswordListsNames().contains(listName)
    }
}