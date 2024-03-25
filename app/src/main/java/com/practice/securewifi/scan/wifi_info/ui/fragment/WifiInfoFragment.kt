package com.practice.securewifi.scan.wifi_info.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.SimpleItemAnimator
import com.practice.securewifi.R
import com.practice.securewifi.app.core.launchOnStarted
import com.practice.securewifi.databinding.FragmentWifiInfoBinding
import com.practice.securewifi.scan.wifi_info.model.WifiInfoUiState
import com.practice.securewifi.scan.wifi_info.ui.adapter.WifiCapabilitiesListAdapter
import com.practice.securewifi.scan.wifi_info.viewmodel.WifiInfoViewModel
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class WifiInfoFragment : Fragment() {

    private var _binding: FragmentWifiInfoBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModel<WifiInfoViewModel>()

    private val adapter by inject<WifiCapabilitiesListAdapter>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWifiInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.wifiCapabilitiesList.adapter = adapter
        (binding.wifiCapabilitiesList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false
        initObservers()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initObservers() {
        viewModel.wifiInfoUiState.onEach { wifiInfoUiState ->
            when (wifiInfoUiState) {
                is WifiInfoUiState.Content -> {
                    binding.wifiSsidTextview.text = wifiInfoUiState.wifiSsid
                    adapter.submitList(wifiInfoUiState.wifiCapabilities)
                }

                WifiInfoUiState.NoInfo -> {
                    binding.wifiSsidTextview.text = getString(R.string.you_are_too_far_from_wifi)
                }
            }
        }.launchOnStarted(lifecycleScope)
    }
}