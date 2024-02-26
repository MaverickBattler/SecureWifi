package com.practice.securewifi.check.passwords_lists_selection.adapter

import androidx.recyclerview.widget.DiffUtil
import com.practice.securewifi.check.passwords_lists_selection.model.PasswordListModel

class PasswordsListSelectionDiffItemCallback : DiffUtil.ItemCallback<PasswordListModel>() {
    override fun areItemsTheSame(
        oldItem: PasswordListModel,
        newItem: PasswordListModel
    ): Boolean {
        return (oldItem.listName == newItem.listName)
    }

    override fun areContentsTheSame(
        oldItem: PasswordListModel,
        newItem: PasswordListModel
    ): Boolean =
        (oldItem == newItem)
}