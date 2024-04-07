package com.practice.securewifi.data.repository

import com.practice.securewifi.data.password_lists.dao.PasswordListDao
import com.practice.securewifi.data.password_lists.dao.PasswordListFixedPasswordDao
import com.practice.securewifi.data.password_lists.dao.PersonInfoDao
import com.practice.securewifi.data.password_lists.dao.PlaceNameDao
import com.practice.securewifi.data.password_lists.entity.PasswordList
import com.practice.securewifi.data.password_lists.entity.PasswordListFixedPassword
import com.practice.securewifi.data.password_lists.entity.PersonInfo
import com.practice.securewifi.data.password_lists.entity.PlaceName
import kotlinx.coroutines.flow.Flow

class PasswordListsRepository(
    private val passwordListFixedPasswordDao: PasswordListFixedPasswordDao,
    private val personInfoDao: PersonInfoDao,
    private val placeNameDao: PlaceNameDao,
    private val passwordListDao: PasswordListDao
) {

    suspend fun getPasswordList(listName: String): PasswordList? {
        return passwordListDao.getPasswordList(listName)
    }

    fun getPasswordListsAsFlow(): Flow<List<PasswordList>> {
        return passwordListDao.getPasswordListsAsFlow()
    }

    suspend fun setPasswordListSelection(listName: String, isSelected: Boolean) {
        val prevPasswordList = passwordListDao.getPasswordList(listName)
        prevPasswordList?.let {
            passwordListDao.insertPasswordList(prevPasswordList.copy(selected = isSelected))
        }
    }

    suspend fun setAllPasswordListsSelection(isSelected: Boolean) {
        val passwordListsToInsert =
            passwordListDao.getPasswordLists().map { it.copy(selected = isSelected) }
        passwordListDao.insertPasswordLists(passwordListsToInsert)
    }

    suspend fun getPasswordLists(): List<PasswordList> {
        return passwordListDao.getPasswordLists()
    }

    suspend fun deletePasswordList(listName: String) {
        passwordListDao.deletePasswordList(listName)
        passwordListFixedPasswordDao.deleteFixedPasswordsForList(listName)
        personInfoDao.deleteAllPersonInfoForList(listName)
        placeNameDao.deleteAllPlaceNamesForList(listName)
    }

    suspend fun saveList(
        newList: PasswordList,
        fixedPasswordsList: List<String>,
        personInfo: List<PersonInfo>,
        placesNames: List<PlaceName>
    ) {
        passwordListDao.insertPasswordList(newList)
        val passwordListFixedPasswordsList =
            fixedPasswordsList.map { PasswordListFixedPassword(newList.listName, it) }
        passwordListFixedPasswordDao.deleteFixedPasswordsForList(newList.listName)
        passwordListFixedPasswordDao.insertFixedPasswordsForList(passwordListFixedPasswordsList)
        personInfoDao.deleteAllPersonInfoForList(newList.listName)
        personInfoDao.insertPersonInfo(personInfo)
        placeNameDao.deleteAllPlaceNamesForList(newList.listName)
        placeNameDao.insertPlacesNames(placesNames)
    }
}