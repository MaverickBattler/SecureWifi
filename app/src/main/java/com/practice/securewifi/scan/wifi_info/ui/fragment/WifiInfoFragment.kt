package com.practice.securewifi.scan.wifi_info.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.SimpleItemAnimator
import com.practice.securewifi.databinding.FragmentWifiInfoBinding
import com.practice.securewifi.scan.wifi_info.model.WifiInfoUiState
import com.practice.securewifi.scan.wifi_info.ui.adapter.WifiCapabilitiesListAdapter
import com.practice.securewifi.scan.wifi_info.viewmodel.WifiInfoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WifiInfoFragment : Fragment() {

    private var _binding: FragmentWifiInfoBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModel<WifiInfoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWifiInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initObservers()
        (binding.wifiCapabilitiesList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initObservers() {
        viewModel.wifiInfoUiState.observe(viewLifecycleOwner) { wifiInfoUiState ->
            if (wifiInfoUiState is WifiInfoUiState.Content) {
                binding.wifiSsidTextview.text = wifiInfoUiState.wifiSsid
                val adapter = WifiCapabilitiesListAdapter()
                binding.wifiCapabilitiesList.adapter = adapter
                adapter.submitList(wifiInfoUiState.wifiCapabilities)
                binding.showAttackResults.isVisible = wifiInfoUiState.buttonCheckResultsVisible
            }
        }
    }
}