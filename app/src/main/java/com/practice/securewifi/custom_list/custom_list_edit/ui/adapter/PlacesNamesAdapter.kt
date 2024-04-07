package com.practice.securewifi.custom_list.custom_list_edit.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.practice.securewifi.custom_list.custom_list_edit.ui.adapter.diffutil.PlaceNameDiffItemCallback
import com.practice.securewifi.data.password_lists.entity.PlaceName
import com.practice.securewifi.databinding.PlaceNameItemBinding

class PlacesNamesAdapter(
    private val isListEditable: Boolean,
    private val onDeleteClickListener: (Int) -> Unit,
) : ListAdapter<PlaceName, PlacesNamesAdapter.PlacesNamesListViewHolder>(
    PlaceNameDiffItemCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesNamesListViewHolder =
        PlacesNamesListViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: PlacesNamesListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onDeleteClickListener, isListEditable)
    }

    class PlacesNamesListViewHolder(
        private val binding: PlaceNameItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): PlacesNamesListViewHolder {
                return PlacesNamesListViewHolder(
                    PlaceNameItemBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: PlaceName, onDeleteClickListener: (Int) -> Unit, isListEditable: Boolean) {
            binding.placeName.text = item.placeName
            binding.personInfoDeleteImageview.isVisible = isListEditable
            binding.personInfoDeleteImageview.setOnClickListener {
                onDeleteClickListener(item.id)
            }
        }
    }
}