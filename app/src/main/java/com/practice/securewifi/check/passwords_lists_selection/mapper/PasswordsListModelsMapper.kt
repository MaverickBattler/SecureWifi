package com.practice.securewifi.check.passwords_lists_selection.mapper

import com.practice.securewifi.check.passwords_lists_selection.model.PasswordListModel
import com.practice.securewifi.data.entity.PasswordList

class PasswordsListModelsMapper {

    fun map(passwordLists: List<PasswordList>): List<PasswordListModel> {
        return passwordLists.map { passwordList ->
            PasswordListModel(
                listName = passwordList.listName,
                selected = passwordList.selected
            )
        }
    }
}