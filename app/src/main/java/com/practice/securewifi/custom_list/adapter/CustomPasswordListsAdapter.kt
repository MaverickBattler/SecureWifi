package com.practice.securewifi.custom_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.practice.securewifi.custom_list.model.CustomPasswordList
import com.practice.securewifi.databinding.CustomPasswordListItemBinding

class CustomPasswordListsAdapter(
    private val onDeleteClickListener: (String) -> Unit,
    private val onItemClickListener: (CustomPasswordList) -> Unit
) : ListAdapter<CustomPasswordList, CustomPasswordListsAdapter.CustomPasswordListsViewHolder>(
    CustomPasswordListDiffCallback()
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomPasswordListsViewHolder =
        CustomPasswordListsViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: CustomPasswordListsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onDeleteClickListener, onItemClickListener)
    }

    class CustomPasswordListsViewHolder(
        private val binding: CustomPasswordListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): CustomPasswordListsViewHolder {
                return CustomPasswordListsViewHolder(
                    CustomPasswordListItemBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
        }

        fun bind(
            item: CustomPasswordList,
            onDeleteClickListener: (String) -> Unit,
            onItemClickListener: (CustomPasswordList) -> Unit
        ) {
            binding.passwordList.text = item.listName
            binding.passwordListDeleteImageview.isVisible = item.deletable
            binding.passwordListDeleteImageview.setOnClickListener {
                onDeleteClickListener(item.listName)
            }
            binding.root.setOnClickListener {
                onItemClickListener(item)
            }
        }
    }
}