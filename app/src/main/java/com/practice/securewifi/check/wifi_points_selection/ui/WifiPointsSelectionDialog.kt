package com.practice.securewifi.check.wifi_points_selection.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.SimpleItemAnimator
import com.practice.securewifi.app.core.base.BaseDialogFragment
import com.practice.securewifi.check.wifi_points_selection.adapter.WifiesSelectionAdapter
import com.practice.securewifi.check.wifi_points_selection.model.WifiListState
import com.practice.securewifi.check.wifi_points_selection.viewmodel.WifiPointsSelectionViewModel
import com.practice.securewifi.databinding.DialogWifiPointsSelectionBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class WifiPointsSelectionDialog : BaseDialogFragment() {

    private var _binding: DialogWifiPointsSelectionBinding? = null

    private val binding get() = _binding!!

    override val dialogWidth: Float = 100f
    override val dialogHeight: Float = 80f

    private val viewModel by viewModel<WifiPointsSelectionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogWifiPointsSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = WifiesSelectionAdapter { wifiPointItem ->
            viewModel.onWifiInListClicked(wifiPointItem)
        }
        viewModel.wifiList
            .onEach { wifiListState ->
                when (wifiListState) {
                    is WifiListState.WifiList -> {
                        binding.progressBar.isVisible = false
                        adapter.submitList(wifiListState.wifiPointItems)
                    }

                    is WifiListState.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                }
            }
            .flowOn(Dispatchers.Main)
            .launchIn(lifecycleScope)
        (binding.wifiList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.wifiList.adapter = adapter
        binding.wifiList.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.buttonSelectAll.setOnClickListener {
            viewModel.onSelectAllButtonClicked(adapter.currentList)
        }
        binding.buttonApply.setOnClickListener {
            dismiss()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        viewModel.startScan()
        super.onStart()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val TAG = "WifiPointsSelectionDialog"
    }
}