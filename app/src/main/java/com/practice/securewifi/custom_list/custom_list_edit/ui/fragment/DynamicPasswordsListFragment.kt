package com.practice.securewifi.custom_list.custom_list_edit.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SeekBar
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.practice.securewifi.R
import com.practice.securewifi.core.extensions.hideKeyboardIfOpened
import com.practice.securewifi.core.extensions.launchOnStarted
import com.practice.securewifi.core.extensions.safeCastToInt
import com.practice.securewifi.core.extensions.setViewsThatHideKeyboardOnClick
import com.practice.securewifi.custom_list.custom_list_edit.ui.adapter.PasswordTypesPagerAdapter
import com.practice.securewifi.custom_list.custom_list_edit.ui.adapter.PersonInfoListAdapter
import com.practice.securewifi.custom_list.custom_list_edit.ui.adapter.PlacesNamesAdapter
import com.practice.securewifi.custom_list.custom_list_edit.ui.input_filter.MinMaxEditTextInputFilter
import com.practice.securewifi.custom_list.custom_list_edit.ui.input_filter.MinMaxEditTextValues
import com.practice.securewifi.custom_list.custom_list_edit.viewmodel.DynamicPasswordsListViewModel
import com.practice.securewifi.databinding.FragmentDynamicPasswordsListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class DynamicPasswordsListFragment : BaseViewPagerFragment() {

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

        val personInfoListAdapter = PersonInfoListAdapter(listEditable) { id ->
            viewModel.onDeletePersonInfoFromList(id)
        }
        val placesNamesAdapter = PlacesNamesAdapter(listEditable) { id ->
            viewModel.onDeletePlaceNameFromList(id)
        }
        binding.personInfoList.adapter = personInfoListAdapter
        binding.placesNamesList.adapter = placesNamesAdapter

        setEditTextFilters()

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


        if (!listEditable) {
            binding.addPersonInfoLayout.isVisible = false
            binding.addPlaceNameLayout.isVisible = false
            binding.separator.isVisible = false
            binding.dynamicPasswordsAmtEditText.isEnabled = false
            binding.seekBarPasswordsAmt.isEnabled = false
        }


        viewModel.presenceOfInfo.onEach { presenceOfInfo ->
            val isTherePersonInfo = presenceOfInfo.isTherePersonInfo
            val isTherePlaceName = presenceOfInfo.isTherePlaceName
            val isAmountOfGeneratedPasswordsPositive = presenceOfInfo.isAmountOfGeneratedPasswordsPositive
            if (!isAmountOfGeneratedPasswordsPositive && !listEditable) {
                // List is not editable and there are no generated passwords amount set or set to 0
                binding.addPlaceNameLayout.isVisible = false
                binding.textviewAddPlaceNameLabel.isVisible = false
                binding.separator.isVisible = false
                binding.addPersonInfoLayout.isVisible = false
                binding.textviewAddPersonInfoLabel.text =
                    getString(R.string.no_dynamically_generated_passwords_for_this_list)
                binding.dynamicPasswordsAmtEditText.isVisible = false
                binding.seekBarPasswordsAmt.isVisible = false
                binding.amountOfGeneratedPasswords.isVisible = false
            } else {
                if (!isTherePersonInfo && !listEditable) {
                    // Amount of generated passwords is >0, but no person info provided for a fixed list
                    binding.separator.isVisible = false
                    binding.addPersonInfoLayout.isVisible = false
                    binding.textviewAddPersonInfoLabel.isVisible = false
                }
                if (!isTherePlaceName && !listEditable) {
                    // Amount of generated passwords is >0, but no place names (keywords) provided for a fixed list
                    binding.addPlaceNameLayout.isVisible = false
                    binding.textviewAddPlaceNameLabel.isVisible = false
                    binding.separator.isVisible = false
                }
            }
        }.launchOnStarted(lifecycleScope)

        viewModel.personInfoList.onEach { personInfoList ->
            personInfoListAdapter.submitList(personInfoList)
        }.launchOnStarted(lifecycleScope)
        viewModel.placesNamesList.onEach { placesNamesList ->
            placesNamesAdapter.submitList(placesNamesList)
        }.launchOnStarted(lifecycleScope)
        viewModel.amountOfGeneratedPasswords.onEach { amountOfGeneratedPasswords ->
            if (amountOfGeneratedPasswords > binding.seekBarPasswordsAmt.max) {
                binding.seekBarPasswordsAmt.progress = binding.seekBarPasswordsAmt.max
            } else {
                binding.seekBarPasswordsAmt.progress = amountOfGeneratedPasswords
            }
            if (binding.dynamicPasswordsAmtEditText.text.toString() != amountOfGeneratedPasswords.toString()) {
                binding.dynamicPasswordsAmtEditText.setText(amountOfGeneratedPasswords.toString())
                binding.dynamicPasswordsAmtEditText.setSelection(binding.dynamicPasswordsAmtEditText.length())
            }
        }.launchOnStarted(lifecycleScope)
        binding.seekBarPasswordsAmt.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                wasChangeFromUser: Boolean
            ) {
                if (wasChangeFromUser) {
                    viewModel.onChangeAmountOfGeneratedPasswords(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                /* do nothing */
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                /* do nothing */
            }
        })
        binding.dynamicPasswordsAmtEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                /* do nothing */
            }

            override fun onTextChanged(newText: CharSequence, start: Int, before: Int, count: Int) {
                if (newText.isEmpty()) {
                    viewModel.onChangeAmountOfGeneratedPasswords(0)
                    binding.dynamicPasswordsAmtEditText.setText(0.toString())
                    binding.dynamicPasswordsAmtEditText.setSelection(binding.dynamicPasswordsAmtEditText.length())
                } else {
                    val newDynamicPasswordsAmt = newText.toString().safeCastToInt()
                    newDynamicPasswordsAmt?.let {
                        viewModel.onChangeAmountOfGeneratedPasswords(it)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                /* do nothing */
            }
        })


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

    override fun onPause() {
        makeViewAppear(binding.cover)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
        lifecycleScope.launch(Dispatchers.Main) {
            delay(50)
            fadeViewOut(binding.cover)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun setEditTextFilters() {
        setOnlyLettersInputFilter(binding.newNameEditText)
        setOnlyLettersInputFilter(binding.newSecondNameEditText)
        setOnlyLettersInputFilter(binding.newFatherOrMiddleNameEditText)
        binding.newDayEditText.filters = arrayOf(
            MinMaxEditTextInputFilter(
                MinMaxEditTextValues.DAY_MIN,
                MinMaxEditTextValues.DAY_MAX
            )
        )
        binding.newMonthEditText.filters = arrayOf(
            MinMaxEditTextInputFilter(
                MinMaxEditTextValues.MONTH_MIN,
                MinMaxEditTextValues.MONTH_MAX
            )
        )
        binding.newYearEditText.filters = arrayOf(
            MinMaxEditTextInputFilter(
                MinMaxEditTextValues.YEAR_MIN,
                MinMaxEditTextValues.YEAR_MAX
            )
        )
        binding.dynamicPasswordsAmtEditText.filters = arrayOf(
            MinMaxEditTextInputFilter(
                MinMaxEditTextValues.PASSWORDS_AMT_MIN,
                MinMaxEditTextValues.PASSWORDS_AMT_MAX
            )
        )
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