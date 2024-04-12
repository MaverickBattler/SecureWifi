package com.practice.securewifi.custom_list.mapper

import android.app.Application
import com.practice.securewifi.R
import com.practice.securewifi.custom_list.model.CustomPasswordList
import com.practice.securewifi.data.password_lists.entity.PasswordList

class CustomPasswordListsMapper(
    private val application: Application
) {

    fun map(passwordList: PasswordList, fixedPasswordsForList: List<String>): CustomPasswordList {
        val fixedPasswordsAmt = fixedPasswordsForList.size + passwordList.amountOfGeneratedPasswords
        val passwordsAmtString = application.getString(R.string.passwords_amt, fixedPasswordsAmt)
        return CustomPasswordList(passwordList.listName, passwordsAmtString, passwordList.deletable)
    }
}