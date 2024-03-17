package com.practice.securewifi.check.passwords_lists_selection.mapper

import android.app.Application
import com.practice.securewifi.R
import com.practice.securewifi.check.passwords_lists_selection.model.PasswordListModel
import com.practice.securewifi.data.entity.PasswordList

class PasswordsListModelsMapper(
    private val application: Application
) {

    fun map(passwordList: PasswordList, passwordsForList: List<String>): PasswordListModel {
        val passwordsAmt = passwordsForList.size
        val passwordsAmtString = application.getString(R.string.passwords_amt, passwordsAmt)
        return PasswordListModel(
            listName = passwordList.listName,
            passwordsAmtString = passwordsAmtString,
            selected = passwordList.selected
        )
    }
}