package com.practice.securewifi.check_results.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.practice.securewifi.core.diffutil.StringDiffUtilCallback
import com.practice.securewifi.databinding.TriedPasswordItemBinding

class WifiAttackResultsAdapter :
    ListAdapter<String, WifiAttackResultsAdapter.WifiAttackResultViewHolder>(
        StringDiffUtilCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WifiAttackResultViewHolder =
        WifiAttackResultViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: WifiAttackResultViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class WifiAttackResultViewHolder(private val binding: TriedPasswordItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): WifiAttackResultViewHolder {
                return WifiAttackResultViewHolder(
                    TriedPasswordItemBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
        }

        fun bind(item: String) {
            binding.password.text = item
        }
    }
}