package com.practice.securewifi.custom_list.custom_list_edit.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.practice.securewifi.R
import com.practice.securewifi.custom_list.custom_list_edit.ui.adapter.PasswordTypesPagerAdapter
import com.practice.securewifi.custom_list.custom_list_edit.viewmodel.CustomPasswordListViewModel
import com.practice.securewifi.databinding.FragmentPasswordListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class CustomPasswordListFragment : Fragment() {

    private var _binding: FragmentPasswordListBinding? = null
    private val binding get() = _binding!!

    private val args: CustomPasswordListFragmentArgs by navArgs()

    private val listName by lazy { args.customListName }

    private val isListEditable by lazy { args.isListEditable }

    private val viewModel: CustomPasswordListViewModel by viewModel { parametersOf(listName) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasswordListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val pagerAdapter = PasswordTypesPagerAdapter(
            listName,
            isListEditable,
            childFragmentManager,
            lifecycle
        )
        binding.viewPager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ ->
            /* do nothing */
        }.attach()
        binding.saveListButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val listName = binding.listNameEditText.text.toString()
                when (viewModel.onSaveList(listName)) {
                    CustomPasswordListViewModel.SaveResult.SUCCESS -> {
                        withContext(Dispatchers.Main) {
                            findNavController().popBackStack()
                        }
                    }

                    CustomPasswordListViewModel.SaveResult.NO_LIST_NAME_PROVIDED -> {
                        withContext(Dispatchers.Main) {
                            binding.listNameContainer.isErrorEnabled = true
                            binding.listNameContainer.error = getString(R.string.enter_list_name)
                        }
                    }

                    CustomPasswordListViewModel.SaveResult.LIST_NAME_ALREADY_EXIST -> {
                        withContext(Dispatchers.Main) {
                            binding.listNameContainer.isErrorEnabled = true
                            binding.listNameContainer.error =
                                getString(R.string.list_name_already_exist)
                        }
                    }
                }
            }
        }
        binding.listNameEditText.setText(listName)
        if (!isListEditable) {
            binding.listNameEditText.isClickable = false
            binding.listNameEditText.isFocusable = false
            binding.listNameEditText.textAlignment = EditText.TEXT_ALIGNMENT_CENTER
            binding.saveListButton.isVisible = false
            binding.listNameContainer.isHintEnabled = false
        }
        binding.listNameEditText.doAfterTextChanged {
            binding.listNameContainer.isErrorEnabled = false
            binding.listNameContainer.error = null
        }

        binding.viewPager.isUserInputEnabled = false
        
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.setText(R.string.fixed_passwords)
                }

                1 -> {
                    tab.setText(R.string.generated_passwords)
                }
            }
        }.attach()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}