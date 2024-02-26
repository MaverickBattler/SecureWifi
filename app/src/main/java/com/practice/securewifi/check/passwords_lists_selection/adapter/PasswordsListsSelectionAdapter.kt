package com.practice.securewifi.check.passwords_lists_selection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.practice.securewifi.check.passwords_lists_selection.model.PasswordListModel
import com.practice.securewifi.databinding.PasswordsListChoosingItemBinding

class PasswordsListsSelectionAdapter(private val onItemClickListener: (PasswordListModel) -> Unit) :
    ListAdapter<PasswordListModel, PasswordsListsSelectionAdapter.PasswordsListsSelectionViewHolder>(
        PasswordsListSelectionDiffItemCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordsListsSelectionViewHolder =
        PasswordsListsSelectionViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: PasswordsListsSelectionViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemClickListener)
    }

    class PasswordsListsSelectionViewHolder(private val binding: PasswordsListChoosingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): PasswordsListsSelectionViewHolder {
                return PasswordsListsSelectionViewHolder(
                    PasswordsListChoosingItemBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
        }

        fun bind(item: PasswordListModel, onItemClickListener: ((PasswordListModel) -> Unit)) {
            binding.root.setOnClickListener {
                onItemClickListener(item)
            }
            binding.passwordList.text = item.listName
            binding.passwordListAddCheckbox.isChecked = item.selected
        }
    }
}