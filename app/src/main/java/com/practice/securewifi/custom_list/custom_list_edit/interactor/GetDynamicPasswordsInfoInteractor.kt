package com.practice.securewifi.custom_list.custom_list_edit.interactor

import com.practice.securewifi.data.password_lists.entity.PersonInfo
import com.practice.securewifi.data.password_lists.entity.PlaceName
import com.practice.securewifi.data.password_lists.repository.PasswordListDynamicPasswordsInfoRepository

class GetDynamicPasswordsInfoInteractor(
    private val passwordListDynamicPasswordsInfoRepository: PasswordListDynamicPasswordsInfoRepository
) {

    suspend fun getPersonInfoForList(listName: String): List<PersonInfo> {
        return passwordListDynamicPasswordsInfoRepository.getPersonInfo(listName)
    }

    suspend fun getPlacesNamesForList(listName: String): List<PlaceName> {
        return passwordListDynamicPasswordsInfoRepository.getPlacesNames(listName)
    }
}