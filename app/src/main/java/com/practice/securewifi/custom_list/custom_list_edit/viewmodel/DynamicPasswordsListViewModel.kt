package com.practice.securewifi.custom_list.custom_list_edit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.securewifi.custom_list.custom_list_edit.interactor.DynamicPasswordsInfoInteractor
import com.practice.securewifi.custom_list.custom_list_edit.interactor.GetDynamicPasswordsInfoInteractor
import com.practice.securewifi.data.password_lists.entity.PersonInfo
import com.practice.securewifi.data.password_lists.entity.PlaceName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DynamicPasswordsListViewModel(
    private val listName: String,
    private val dynamicPasswordsInfoInteractor: DynamicPasswordsInfoInteractor,
    private val getDynamicPasswordsInfoInteractor: GetDynamicPasswordsInfoInteractor
) : ViewModel() {

    val personInfoList: StateFlow<List<PersonInfo>> = dynamicPasswordsInfoInteractor.personInfoList
    val placesNamesList: StateFlow<List<PlaceName>> = dynamicPasswordsInfoInteractor.placesNamesList

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val storedPersonInfo = getDynamicPasswordsInfoInteractor.getPersonInfoForList(listName)
            val storedPlacesNames =
                getDynamicPasswordsInfoInteractor.getPlacesNamesForList(listName)
            dynamicPasswordsInfoInteractor.updateDynamicPasswordsInfo(
                storedPersonInfo,
                storedPlacesNames
            )
        }
    }

    fun onDeletePersonInfoFromList(personInfoId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dynamicPasswordsInfoInteractor.deletePersonInfoFromList(personInfoId)
        }
    }

    fun onDeletePlaceNameFromList(placeNameId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dynamicPasswordsInfoInteractor.deletePlaceNameFromList(placeNameId)
        }
    }

    fun onAddNewPersonInfoToList(
        name: String? = null,
        secondName: String? = null,
        fatherOrMiddleName: String? = null,
        day: Int? = null,
        month: Int? = null,
        year: Int? = null
    ): AddStatus {
        if (name.isNullOrEmpty() && secondName.isNullOrEmpty() && fatherOrMiddleName.isNullOrEmpty()
            && day == null && month == null && year == null) {
            return AddStatus.NO_DATA_PROVIDED
        }
        viewModelScope.launch(Dispatchers.IO) {
            dynamicPasswordsInfoInteractor.addPersonInfo(
                listName,
                name,
                secondName,
                fatherOrMiddleName,
                day,
                month,
                year
            )
        }
        return AddStatus.SUCCESS
    }

    fun onAddNewPlaceNameToList(placeName: String): AddStatus {
        if (placeName == "") {
            return AddStatus.NO_DATA_PROVIDED
        }
        viewModelScope.launch(Dispatchers.IO) {
            dynamicPasswordsInfoInteractor.addPlaceName(listName, placeName)
        }
        return AddStatus.SUCCESS
    }

    enum class AddStatus {
        SUCCESS,
        NO_DATA_PROVIDED
    }
}