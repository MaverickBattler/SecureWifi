package com.practice.securewifi.custom_list.custom_list_edit.ui.fragment

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.core.text.bold
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.practice.securewifi.R
import com.practice.securewifi.core.extensions.collectOnStarted
import com.practice.securewifi.core.extensions.setViewsThatHideKeyboardOnClick
import com.practice.securewifi.custom_list.custom_list_edit.ui.adapter.FixedPasswordsAdapter
import com.practice.securewifi.custom_list.custom_list_edit.ui.adapter.PasswordTypesPagerAdapter
import com.practice.securewifi.custom_list.custom_list_edit.viewmodel.FixedPasswordsListViewModel
import com.practice.securewifi.databinding.FragmentFixedPasswordsListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FixedPasswordsListFragment : BaseViewPagerFragment() {

    private var _binding: FragmentFixedPasswordsListBinding? = null
    private val binding get() = _binding!!

    private val listName by lazy { arguments?.getString(PasswordTypesPagerAdapter.PASSWORD_LIST_NAME_KEY) }

    private val isListEditable by lazy { arguments?.getBoolean(PasswordTypesPagerAdapter.IS_LIST_EDITABLE_KEY) }

    private val viewModel: FixedPasswordsListViewModel by viewModel { parametersOf(listName) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFixedPasswordsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val onDeleteClickListener: (String) -> Unit = { password ->
            viewModel.onDeletePasswordFromList(password)
        }
        isListEditable?.let { isListEditable ->
            val adapter = FixedPasswordsAdapter(onDeleteClickListener, isListEditable)
            binding.passwordList.adapter = adapter
            binding.passwordList.addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
            viewModel.fixedPasswordsList.collectOnStarted(lifecycleScope, lifecycle) { passwordList ->
                val s = SpannableStringBuilder()
                    .append(getString(R.string.amount_of_fixed_passwords))
                    .bold { append(passwordList.size.toString()) }
                binding.amountOfFixedPasswords.text = s
                adapter.submitList(passwordList)
            }
            binding.buttonAdd.setOnClickListener {
                viewModel.onAddNewPasswordToList(binding.newPasswordEditText.text.toString())
                clearNewPasswordEditText()
            }
            if (!isListEditable) {
                binding.addPasswordLayout.isVisible = false
                // set bottom padding of textview to 0
                binding.amountOfFixedPasswords.setPadding(
                    binding.amountOfFixedPasswords.paddingLeft,
                    binding.amountOfFixedPasswords.paddingTop,
                    binding.amountOfFixedPasswords.paddingRight,
                    getDimenInPx(R.dimen.padding_small)
                )
            }

            setViewsThatHideKeyboardOnClick(listOf(binding.root))
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getDimenInPx(@DimenRes dimenRes: Int): Int {
        return resources.getDimensionPixelSize(dimenRes)
    }

    private fun clearNewPasswordEditText() {
        binding.newPasswordEditText.setText("")
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
}