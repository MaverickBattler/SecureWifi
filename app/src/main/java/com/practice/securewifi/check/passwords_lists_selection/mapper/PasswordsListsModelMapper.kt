package com.practice.securewifi.check.passwords_lists_selection.mapper

import com.practice.securewifi.check.passwords_lists_selection.model.PasswordListModel
import com.practice.securewifi.data.entity.PasswordList

class PasswordsListsModelMapper {

    fun map(passwordList: PasswordList): PasswordListModel {
        return PasswordListModel(
            listName = passwordList.listName,
            selected = passwordList.selected
        )
    }
}