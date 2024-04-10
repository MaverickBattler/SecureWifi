package com.practice.securewifi.custom_list.custom_list_edit.interactor

import com.practice.securewifi.data.password_lists.entity.PersonInfo
import com.practice.securewifi.data.password_lists.entity.PlaceName
import com.practice.securewifi.data.password_lists.repository.PasswordListDynamicPasswordsInfoRepository
import com.practice.securewifi.data.repository.PasswordListsRepository

class GetDynamicPasswordsInfoInteractor(
    private val passwordListDynamicPasswordsInfoRepository: PasswordListDynamicPasswordsInfoRepository,
    private val passwordListsRepository: PasswordListsRepository
) {

    suspend fun getAmountOfGeneratedPasswordsForList(listName: String): Int {
        return passwordListsRepository.getAmountOfGeneratedPasswords(listName)
    }

    suspend fun getPersonInfoForList(listName: String): List<PersonInfo> {
        return passwordListDynamicPasswordsInfoRepository.getPersonInfo(listName)
    }

    suspend fun getPlacesNamesForList(listName: String): List<PlaceName> {
        return passwordListDynamicPasswordsInfoRepository.getPlacesNames(listName)
    }
}