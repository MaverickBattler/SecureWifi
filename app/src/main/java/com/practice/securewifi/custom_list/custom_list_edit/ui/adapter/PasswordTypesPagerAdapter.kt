package com.practice.securewifi.custom_list.custom_list_edit.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practice.securewifi.custom_list.custom_list_edit.ui.fragment.DynamicPasswordsListFragment
import com.practice.securewifi.custom_list.custom_list_edit.ui.fragment.FixedPasswordsListFragment

class PasswordTypesPagerAdapter(
    private val passwordListName: String,
    private val isListEditable: Boolean,
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                FixedPasswordsListFragment().apply {
                    arguments = Bundle().apply {
                        putString(PASSWORD_LIST_NAME_KEY, passwordListName)
                        putBoolean(IS_LIST_EDITABLE_KEY, isListEditable)
                    }
                }
            }

            else -> {
                DynamicPasswordsListFragment().apply {
                    arguments = Bundle().apply {
                        putString(PASSWORD_LIST_NAME_KEY, passwordListName)
                        putBoolean(IS_LIST_EDITABLE_KEY, isListEditable)
                    }
                }
            }
        }
    }

    companion object {
        const val PASSWORD_LIST_NAME_KEY = "PasswordListName"
        const val IS_LIST_EDITABLE_KEY = "IsListEditable"
    }
}