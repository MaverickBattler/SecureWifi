package com.practice.securewifi.custom_list.custom_list_edit.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.practice.securewifi.custom_list.custom_list_edit.ui.adapter.diffutil.PersonInfoDiffItemCallback
import com.practice.securewifi.data.password_lists.entity.PersonInfo
import com.practice.securewifi.databinding.PersonInfoItemBinding

class PersonInfoListAdapter(
    private val isListEditable: Boolean,
    private val onDeleteClickListener: (Int) -> Unit,
) : ListAdapter<PersonInfo, PersonInfoListAdapter.PersonInfoListViewHolder>(
    PersonInfoDiffItemCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonInfoListViewHolder =
        PersonInfoListViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: PersonInfoListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onDeleteClickListener, isListEditable)
    }

    class PersonInfoListViewHolder(
        private val binding: PersonInfoItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): PersonInfoListViewHolder {
                return PersonInfoListViewHolder(
                    PersonInfoItemBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: PersonInfo, onDeleteClickListener: (Int) -> Unit, isListEditable: Boolean) {
            // Convert name, secondName and fatherOrMiddleName to null or content for easier comprasion
            val name = if (item.name.isNullOrEmpty()) {
                null
            } else {
                item.name
            }
            val secondName = if (item.secondName.isNullOrEmpty()) {
                null
            } else {
                item.secondName
            }
            val fatherOrMiddleName = if (item.fatherOrMiddleName.isNullOrEmpty()) {
                null
            } else {
                item.fatherOrMiddleName
            }
            val nameStr =
                when {
                    name != null && fatherOrMiddleName != null && secondName != null -> {
                        item.name + " " + item.fatherOrMiddleName + " " + item.secondName
                    }
                    name != null && secondName != null -> {
                        item.name + " " + item.secondName
                    }
                    name == null && secondName == null && fatherOrMiddleName == null -> {
                        ""
                    }
                    else -> {
                        (name ?: "...") + " " + (fatherOrMiddleName ?: "...") + " " + (secondName ?: "...")
                    }
                }
            val dateStr =
                if (item.day == null && item.month == null && item.year == null) {
                    ""
                } else if (nameStr != ""){
                    " | " + (item.day ?: "xx") + "." + (item.month ?: "xx") + "." + (item.year
                        ?: "xxxx")
                } else {
                    (item.day ?: "xx").toString() + "." + (item.month ?: "xx") + "." + (item.year
                        ?: "xxxx")
                }
            binding.personInfo.text = nameStr + dateStr
            binding.personInfoDeleteImageview.isVisible = isListEditable
            binding.personInfoDeleteImageview.setOnClickListener {
                onDeleteClickListener(item.id)
            }
        }
    }
}