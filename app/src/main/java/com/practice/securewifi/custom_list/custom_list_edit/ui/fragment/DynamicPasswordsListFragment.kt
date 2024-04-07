package com.practice.securewifi.custom_list.custom_list_edit.ui.fragment

import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.practice.securewifi.R
import com.practice.securewifi.core.extensions.hideKeyboardIfOpened
import com.practice.securewifi.core.extensions.launchOnStarted
import com.practice.securewifi.core.extensions.safeCastToInt
import com.practice.securewifi.core.extensions.setViewsThatHideKeyboardOnClick
import com.practice.securewifi.custom_list.custom_list_edit.ui.adapter.PasswordTypesPagerAdapter
import com.practice.securewifi.custom_list.custom_list_edit.ui.adapter.PersonInfoListAdapter
import com.practice.securewifi.custom_list.custom_list_edit.ui.adapter.PlacesNamesAdapter
import com.practice.securewifi.custom_list.custom_list_edit.viewmodel.DynamicPasswordsListViewModel
import com.practice.securewifi.databinding.FragmentDynamicPasswordsListBinding
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class DynamicPasswordsListFragment : Fragment() {

    private var _binding: FragmentDynamicPasswordsListBinding? = null
    private val binding get() = _binding!!

    private val listName by lazy { arguments?.getString(PasswordTypesPagerAdapter.PASSWORD_LIST_NAME_KEY) }

    private val isListEditable by lazy { arguments?.getBoolean(PasswordTypesPagerAdapter.IS_LIST_EDITABLE_KEY) }

    private val viewModel: DynamicPasswordsListViewModel by viewModel { parametersOf(listName) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDynamicPasswordsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val listEditable = isListEditable ?: false

        if (!listEditable) {
            binding.addPersonInfoLayout.isVisible = false
            binding.addPlaceNameLayout.isVisible = false
            binding.separator.isVisible = false
            binding.dynamicPasswordsAmtEditText.isEnabled = false
            binding.seekBarPasswordsAmt.isEnabled = false
        }

        val personInfoListAdapter = PersonInfoListAdapter(listEditable) { id ->
            viewModel.onDeletePersonInfoFromList(id)
        }
        val placesNamesAdapter = PlacesNamesAdapter(listEditable) { id ->
            viewModel.onDeletePlaceNameFromList(id)
        }
        binding.personInfoList.adapter = personInfoListAdapter
        binding.placesNamesList.adapter = placesNamesAdapter

        setOnlyLettersInputFilter(binding.newNameEditText)
        setOnlyLettersInputFilter(binding.newSecondNameEditText)
        setOnlyLettersInputFilter(binding.newFatherOrMiddleNameEditText)
        setNumericInputFilter(binding.newDayEditText)
        setNumericInputFilter(binding.newMonthEditText)
        setNumericInputFilter(binding.newYearEditText)

        binding.buttonAddPersonInfo.setOnClickListener {
            hideKeyboardIfOpened()
            val result = viewModel.onAddNewPersonInfoToList(
                name = binding.newNameEditText.text.toString(),
                secondName = binding.newSecondNameEditText.text.toString(),
                fatherOrMiddleName = binding.newFatherOrMiddleNameEditText.text.toString(),
                day = binding.newDayEditText.text.toString().safeCastToInt(),
                month = binding.newMonthEditText.text.toString().safeCastToInt(),
                year = binding.newYearEditText.text.toString().safeCastToInt()
            )
            when (result) {
                DynamicPasswordsListViewModel.AddStatus.SUCCESS -> {
                    binding.textviewAskForFillOnOfFields.isVisible = false
                    clearPersonInfoEditTexts()
                }

                DynamicPasswordsListViewModel.AddStatus.NO_DATA_PROVIDED -> {
                    binding.textviewAskForFillOnOfFields.isVisible = true
                }
            }
        }
        binding.buttonAddPlaceName.setOnClickListener {
            hideKeyboardIfOpened()
            val result = viewModel.onAddNewPlaceNameToList(
                placeName = binding.newPlaceNameEditText.text.toString()
            )
            when (result) {
                DynamicPasswordsListViewModel.AddStatus.SUCCESS -> {
                    binding.newPlaceNameContainer.error = null
                    binding.newPlaceNameContainer.isErrorEnabled = false
                    clearPlaceNameEditText()
                }

                DynamicPasswordsListViewModel.AddStatus.NO_DATA_PROVIDED -> {
                    binding.newPlaceNameContainer.isErrorEnabled = true
                    binding.newPlaceNameContainer.error = getString(R.string.please_fill_this_field)
                }
            }
        }

        if (viewModel.personInfoList.value.isEmpty() && viewModel.placesNamesList.value.isEmpty() && !listEditable) {
            binding.separator.isVisible = false
            binding.addPersonInfoLayout.isVisible = false
            binding.addPlaceNameLayout.isVisible = false
            binding.textviewAddPlaceNameLabel.isVisible = false
            binding.textviewAddPersonInfoLabel.text =
                getString(R.string.no_dynamically_generated_passwords_for_this_list)
        }

        viewModel.personInfoList.onEach { personInfoList ->
            personInfoListAdapter.submitList(personInfoList)
        }.launchOnStarted(lifecycleScope)
        viewModel.placesNamesList.onEach { placesNamesList ->
            placesNamesAdapter.submitList(placesNamesList)
        }.launchOnStarted(lifecycleScope)

        setViewsThatHideKeyboardOnClick(
            listOf(
                binding.amountOfGeneratedPasswords,
                binding.seekBarPasswordsAmt,
                binding.textviewAddPersonInfoLabel,
                binding.addPersonInfoLayout,
                binding.personInfoList,
                binding.textviewAddPlaceNameLabel,
                binding.addPlaceNameLayout,
                binding.placesNamesList
            )
        )

        super.onViewCreated(view, savedInstanceState)
    }

    private fun clearPersonInfoEditTexts() {
        binding.newNameEditText.setText("")
        binding.newSecondNameEditText.setText("")
        binding.newFatherOrMiddleNameEditText.setText("")
        binding.newDayEditText.setText("")
        binding.newMonthEditText.setText("")
        binding.newYearEditText.setText("")
    }

    private fun clearPlaceNameEditText() {
        binding.newPlaceNameEditText.setText("")
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun setNumericInputFilter(editText: EditText) {
        val filter = object : InputFilter {
            override fun filter(
                source: CharSequence?,
                start: Int,
                end: Int,
                dest: Spanned?,
                dstart: Int,
                dend: Int
            ): CharSequence? {
                // Iterate through each character in the source text
                for (i in start until end) {
                    // Check if the character is not a digit
                    if (!Character.isDigit(source!![i])) {
                        return ""
                    }
                }
                return null // Accept the input
            }
        }
        editText.filters = arrayOf(filter)
    }

    private fun setOnlyLettersInputFilter(editText: EditText) {
        val filter = object : InputFilter {
            override fun filter(
                source: CharSequence?,
                start: Int,
                end: Int,
                dest: Spanned?,
                dstart: Int,
                dend: Int
            ): CharSequence? {
                // Iterate through each character in the source text
                for (i in start until end) {
                    // Check if the character is not a digit
                    if (!Character.isLetter(source!![i])) {
                        return ""
                    }
                }
                return null // Accept the input
            }
        }
        editText.filters = arrayOf(filter)
    }
}