package com.practice.securewifi.custom_list.custom_list_edit.repository

import com.practice.securewifi.data.password_lists.entity.PersonInfo
import com.practice.securewifi.data.password_lists.entity.PlaceName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DynamicPasswordsInfoRepository {

    private val _personInfoList: MutableStateFlow<List<PersonInfo>> = MutableStateFlow(emptyList())
    private val _placesNamesList: MutableStateFlow<List<PlaceName>> = MutableStateFlow(emptyList())
    private val _amountOfGeneratedPasswords: MutableStateFlow<Int> = MutableStateFlow(0)

    val personInfoList: StateFlow<List<PersonInfo>> =
        _personInfoList.asStateFlow()
    val placesNamesList: StateFlow<List<PlaceName>> =
        _placesNamesList.asStateFlow()
    val amountOfGeneratedPasswords: StateFlow<Int> =
        _amountOfGeneratedPasswords.asStateFlow()

    suspend fun updateDynamicPasswordsInfo(
        newPersonInfoList: List<PersonInfo>,
        newPlacesNamesList: List<PlaceName>,
        newAmountOfGeneratedPasswords: Int
    ) {
        _personInfoList.emit(newPersonInfoList)
        _placesNamesList.emit(newPlacesNamesList)
        _amountOfGeneratedPasswords.emit(newAmountOfGeneratedPasswords)
    }

    suspend fun deletePersonInfoFromList(personInfoId: Int) {
        _personInfoList.emit(personInfoList.value.filter { it.id != personInfoId })
    }

    suspend fun deletePlaceNameFromList(placeNameId: Int) {
        _placesNamesList.emit(placesNamesList.value.filter { it.id != placeNameId })
    }

    suspend fun addPersonInfo(personInfo: PersonInfo) {
        _personInfoList.emit(personInfoList.value.toMutableList().apply {
            if (personInfoList.value.find {
                    it.day == personInfo.day
                            && it.month == personInfo.month
                            && it.year == personInfo.year
                            && it.name == personInfo.name
                            && it.secondName == personInfo.secondName
                            && it.fatherOrMiddleName == personInfo.fatherOrMiddleName
                } == null) {
                add(personInfo)
            }
        })
    }

    suspend fun addPlaceName(placeName: PlaceName) {
        _placesNamesList.emit(placesNamesList.value.toMutableList().apply {
            if (!placesNamesList.value.contains(placeName)) {
                add(placeName)
            }
        })
    }

    suspend fun setAmountOfGeneratedPasswords(amount: Int) {
        _amountOfGeneratedPasswords.emit(amount)
    }
}