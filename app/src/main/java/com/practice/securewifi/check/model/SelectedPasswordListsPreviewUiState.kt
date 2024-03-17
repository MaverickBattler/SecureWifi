package com.practice.securewifi.check.model

import androidx.annotation.AttrRes

data class SelectedPasswordListsPreviewUiState(
    val listsText: String,
    val totalPasswordsAmtString: String?,
    @AttrRes
    val listsTextColor: Int
)