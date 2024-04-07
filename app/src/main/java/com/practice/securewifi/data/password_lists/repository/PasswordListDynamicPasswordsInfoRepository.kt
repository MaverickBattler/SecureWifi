package com.practice.securewifi.data.password_lists.repository

import com.practice.securewifi.data.password_lists.dao.PersonInfoDao
import com.practice.securewifi.data.password_lists.dao.PlaceNameDao
import com.practice.securewifi.data.password_lists.entity.PersonInfo
import com.practice.securewifi.data.password_lists.entity.PlaceName

class PasswordListDynamicPasswordsInfoRepository(
    private val personInfoDao: PersonInfoDao,
    private val placeNameDao: PlaceNameDao
) {

    suspend fun getPersonInfo(passwordListName: String): List<PersonInfo> {
        return personInfoDao.getPersonInfoForList(passwordListName)
    }

    suspend fun getPlacesNames(passwordListName: String): List<PlaceName> {
        return placeNameDao.getPlaceNamesForList(passwordListName)
    }
}