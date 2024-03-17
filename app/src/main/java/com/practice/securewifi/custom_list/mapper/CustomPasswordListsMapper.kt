package com.practice.securewifi.custom_list.mapper

import android.app.Application
import com.practice.securewifi.R
import com.practice.securewifi.custom_list.model.CustomPasswordList
import com.practice.securewifi.data.entity.PasswordList

class CustomPasswordListsMapper(
    private val application: Application
) {

    fun map(passwordList: PasswordList, passwordsForlist: List<String>): CustomPasswordList {
        val passwordsAmt = passwordsForlist.size
        val passwordsAmtString = application.getString(R.string.passwords_amt, passwordsAmt)
        return CustomPasswordList(passwordList.listName, passwordsAmtString, passwordList.deletable)
    }
}