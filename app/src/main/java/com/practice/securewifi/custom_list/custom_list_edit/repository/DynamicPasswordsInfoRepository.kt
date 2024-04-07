package com.practice.securewifi.custom_list.custom_list_edit.repository

import com.practice.securewifi.data.password_lists.entity.PersonInfo
import com.practice.securewifi.data.password_lists.entity.PlaceName
import kotlinx.coroutines.flow.MutableStateFlow

class DynamicPasswordsInfoRepository {

    val personInfoList: MutableStateFlow<List<PersonInfo>> = MutableStateFlow(emptyList())
    val placesNamesList: MutableStateFlow<List<PlaceName>> = MutableStateFlow(emptyList())

    suspend fun updateDynamicPasswordsInfo(
        newPersonInfoList: List<PersonInfo>,
        newPlacesNamesList: List<PlaceName>
    ) {
        personInfoList.emit(newPersonInfoList)
        placesNamesList.emit(newPlacesNamesList)
    }

    suspend fun deletePersonInfoFromList(personInfoId: Int) {
        personInfoList.emit(
            personInfoList.value.toMutableList().apply { removeIf { it.id == personInfoId } })
    }

    suspend fun deletePlaceNameFromList(placeNameId: Int) {
        placesNamesList.emit(
            placesNamesList.value.toMutableList().apply { removeIf { it.id == placeNameId } })
    }

    suspend fun addPersonInfo(personInfo: PersonInfo) {
        personInfoList.emit(personInfoList.value.toMutableList().apply {
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
        placesNamesList.emit(placesNamesList.value.toMutableList().apply {
            if (!placesNamesList.value.contains(placeName)) {
                add(placeName)
            }
        })
    }
}