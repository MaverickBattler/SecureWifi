package com.practice.securewifi.custom_list.custom_list_edit.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.practice.securewifi.core.diffutil.StringDiffUtilCallback
import com.practice.securewifi.databinding.PasswordListItemBinding

class FixedPasswordsAdapter(
    private val onDeleteClickListener: (String) -> Unit,
    private val isListEditable: Boolean
) : ListAdapter<String, FixedPasswordsAdapter.FixedPasswordsViewHolder>(
    StringDiffUtilCallback()
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FixedPasswordsViewHolder =
        FixedPasswordsViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: FixedPasswordsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onDeleteClickListener, isListEditable)
    }

    class FixedPasswordsViewHolder(
        private val binding: PasswordListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): FixedPasswordsViewHolder {
                return FixedPasswordsViewHolder(
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