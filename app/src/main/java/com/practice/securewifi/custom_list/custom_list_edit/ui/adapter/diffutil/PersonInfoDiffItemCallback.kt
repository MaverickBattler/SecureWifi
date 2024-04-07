package com.practice.securewifi.custom_list.custom_list_edit.ui.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.practice.securewifi.data.password_lists.entity.PersonInfo

class PersonInfoDiffItemCallback : DiffUtil.ItemCallback<PersonInfo>() {
    override fun areItemsTheSame(
        oldItem: PersonInfo,
        newItem: PersonInfo
    ): Boolean = (oldItem.id == newItem.id)

    override fun areContentsTheSame(
        oldItem: PersonInfo,
        newItem: PersonInfo
    ): Boolean = (oldItem == newItem)
}