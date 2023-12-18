package com.practice.securewifi.custom_list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.practice.securewifi.custom_list.model.CustomPasswordList

class CustomPasswordListDiffCallback : DiffUtil.ItemCallback<CustomPasswordList>() {
    override fun areItemsTheSame(
        oldItem: CustomPasswordList,
        newItem: CustomPasswordList
    ): Boolean = (oldItem.listName == newItem.listName)

    override fun areContentsTheSame(
        oldItem: CustomPasswordList,
        newItem: CustomPasswordList
    ): Boolean = (oldItem == newItem)
}