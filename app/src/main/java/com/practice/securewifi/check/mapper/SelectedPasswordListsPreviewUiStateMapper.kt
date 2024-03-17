package com.practice.securewifi.check.mapper

import android.app.Application
import com.practice.securewifi.R
import com.practice.securewifi.check.model.SelectedPasswordListsPreviewUiState

class SelectedPasswordListsPreviewUiStateMapper(
    private val application: Application
) {

    fun map(
        passwordListsNames: List<String>,
        totalPasswordsAmt: Int
    ): SelectedPasswordListsPreviewUiState {
        val listsText: String
        val totalPasswordsAmtString: String?
        val listsTextColor: Int
        when {
            passwordListsNames.isEmpty() -> {
                listsText = application.getString(R.string.no_password_lists_selected)
                totalPasswordsAmtString = null
                listsTextColor = R.attr.textColorInactive
            }

            passwordListsNames.size == 1 -> {
                listsText = passwordListsNames[0]
                totalPasswordsAmtString = application.getString(R.string.total_passwords_amt, totalPasswordsAmt)
                listsTextColor = R.attr.textColorMain
            }

            else -> {
                listsText = application.getString(
                        R.string.something_and_n_others,
                        passwordListsNames[0],
                        passwordListsNames.size - 1
                    )
                totalPasswordsAmtString = application.getString(R.string.total_passwords_amt, totalPasswordsAmt)
                listsTextColor = R.attr.textColorMain
            }
        }
        return SelectedPasswordListsPreviewUiState(
            listsText,
            totalPasswordsAmtString,
            listsTextColor
        )
    }
}