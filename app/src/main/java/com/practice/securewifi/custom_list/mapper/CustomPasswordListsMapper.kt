package com.practice.securewifi.custom_list.mapper

import com.practice.securewifi.custom_list.model.CustomPasswordList
import com.practice.securewifi.data.entity.PasswordList

class CustomPasswordListsMapper {

    fun map(passwordLists: List<PasswordList>): List<CustomPasswordList> {
        return passwordLists.map { passwordList ->
            CustomPasswordList(passwordList.listName, passwordList.deletable)
        }
    }
}