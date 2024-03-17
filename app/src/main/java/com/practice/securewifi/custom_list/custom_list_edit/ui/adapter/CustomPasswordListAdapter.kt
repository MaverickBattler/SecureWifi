package com.practice.securewifi.custom_list.custom_list_edit.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.practice.securewifi.app.core.diffutil.StringDiffUtilCallback
import com.practice.securewifi.databinding.PasswordListItemBinding

class CustomPasswordListAdapter(
    private val onDeleteClickListener: (String) -> Unit,
    private val isListEditable: Boolean
) : ListAdapter<String, CustomPasswordListAdapter.CustomPasswordListViewHolder>(
    StringDiffUtilCallback()
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomPasswordListViewHolder = CustomPasswordListViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: CustomPasswordListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onDeleteClickListener, isListEditable)
    }

    class CustomPasswordListViewHolder(
        private val binding: PasswordListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): CustomPasswordListViewHolder {
                return CustomPasswordListViewHolder(
                    PasswordListItemBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
        }

        fun bind(item: String, onDeleteClickListener: (String) -> Unit, isListEditable: Boolean) {
            binding.password.text = item
            binding.passwordDeleteImageview.isVisible = isListEditable
            binding.passwordDeleteImageview.setOnClickListener {
                onDeleteClickListener(item)
            }
        }
    }
}