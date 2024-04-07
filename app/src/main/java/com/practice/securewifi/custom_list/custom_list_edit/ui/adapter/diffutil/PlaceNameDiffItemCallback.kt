package com.practice.securewifi.custom_list.custom_list_edit.ui.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.practice.securewifi.data.password_lists.entity.PlaceName

class PlaceNameDiffItemCallback : DiffUtil.ItemCallback<PlaceName>() {
    override fun areItemsTheSame(
        oldItem: PlaceName,
        newItem: PlaceName
    ): Boolean = (oldItem.id == newItem.id)

    override fun areContentsTheSame(
        oldItem: PlaceName,
        newItem: PlaceName
    ): Boolean = (oldItem == newItem)
}