package com.practice.securewifi.check.passwords_lists_selection.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.SimpleItemAnimator
import com.practice.securewifi.app.core.base.BaseDialogFragment
import com.practice.securewifi.app.core.launchOnStarted
import com.practice.securewifi.check.passwords_lists_selection.adapter.PasswordsListsSelectionAdapter
import com.practice.securewifi.check.passwords_lists_selection.viewmodel.PasswordsListSelectionViewModel
import com.practice.securewifi.databinding.DialogPasswordsListsSelectionBinding
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class PasswordsListsSelectionDialog : BaseDialogFragment() {

    private var _binding: DialogPasswordsListsSelectionBinding? = null

    private val binding get() = _binding!!

    override val dialogWidth: Float = 100f
    override val dialogHeight: Float = 80f

    private val viewModel by viewModel<PasswordsListSelectionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogPasswordsListsSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = PasswordsListsSelectionAdapter { passwordListModel ->
            viewModel.onPasswordListInListClicked(passwordListModel)
        }
        viewModel.passwordsLists.onEach { passwordsLists ->
            adapter.submitList(passwordsLists)
        }.launchOnStarted(lifecycleScope)
        (binding.passwordsLists.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.passwordsLists.adapter = adapter
        binding.passwordsLists.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.buttonSelectAll.setOnClickListener {
            viewModel.onSelectAllButtonClicked()
        }
        binding.buttonApply.setOnClickListener {
            dismiss()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val TAG = "PasswordsListsSelectionDialog"
    }
}