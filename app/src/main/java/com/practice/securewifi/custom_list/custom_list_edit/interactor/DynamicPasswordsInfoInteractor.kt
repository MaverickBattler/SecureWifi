package com.practice.securewifi.custom_list.custom_list_edit.interactor

import com.practice.securewifi.custom_list.custom_list_edit.repository.DynamicPasswordsInfoRepository
import com.practice.securewifi.data.password_lists.entity.PersonInfo
import com.practice.securewifi.data.password_lists.entity.PlaceName
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DynamicPasswordsInfoInteractor(
    private val dynamicPasswordsInfoRepository: DynamicPasswordsInfoRepository
) {

    val personInfoList: StateFlow<List<PersonInfo>> =
        dynamicPasswordsInfoRepository.personInfoList.asStateFlow()
    val placesNamesList: StateFlow<List<PlaceName>> =
        dynamicPasswordsInfoRepository.placesNamesList.asStateFlow()


    suspend fun updateDynamicPasswordsInfo(
        newPersonInfoList: List<PersonInfo>,
        newPlacesNamesList: List<PlaceName>
    ) {
        dynamicPasswordsInfoRepository.updateDynamicPasswordsInfo(
            newPersonInfoList,
            newPlacesNamesList
        )
    }

    suspend fun deletePersonInfoFromList(personInfoId: Int) {
        dynamicPasswordsInfoRepository.deletePersonInfoFromList(personInfoId)
    }

    suspend fun deletePlaceNameFromList(placeNameId: Int) {
        dynamicPasswordsInfoRepository.deletePlaceNameFromList(placeNameId)
    }

    suspend fun addPersonInfo(
        listName: String,
        name: String? = null,
        secondName: String? = null,
        fatherOrMiddleName: String? = null,
        day: Int? = null,
        month: Int? = null,
        year: Int? = null
    ) {
        val maxId = personInfoList.value.maxByOrNull { it.id }?.id ?: 0
        dynamicPasswordsInfoRepository.addPersonInfo(
            PersonInfo(
                id = maxId + 1,
                listName = listName,
                name = name,
                secondName = secondName,
                fatherOrMiddleName = fatherOrMiddleName,
                day = day,
                month = month,
                year = year
            )
        )
    }

    suspend fun addPlaceName(listName: String, placeName: String) {
        val maxId = personInfoList.value.maxByOrNull { it.id }?.id ?: 0
        dynamicPasswordsInfoRepository.addPlaceName(PlaceName(
            id = maxId,
            listName = listName,
            placeName = placeName
        ))
    }
}