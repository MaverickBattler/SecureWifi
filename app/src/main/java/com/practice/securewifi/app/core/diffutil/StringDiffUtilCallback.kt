package com.practice.securewifi.app.core.diffutil

import androidx.recyclerview.widget.DiffUtil

class StringDiffUtilCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = (oldItem == newItem)

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
        (oldItem == newItem)
}