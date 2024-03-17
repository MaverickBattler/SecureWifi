package com.practice.securewifi.check.passwords_lists_selection.model

data class PasswordListModel(
    val listName: String,
    val passwordsAmtString: String,
    val selected: Boolean
)