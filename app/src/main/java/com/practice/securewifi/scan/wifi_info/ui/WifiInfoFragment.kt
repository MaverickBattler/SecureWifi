package com.practice.securewifi.scan.wifi_info.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practice.securewifi.databinding.FragmentWifiInfoBinding
import com.practice.securewifi.scan.wifi_info.viewmodel.WifiInfoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

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
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initObservers() {
        viewModel.wifiInfo.observe(viewLifecycleOwner) { scanResult ->
            Timber.i(scanResult?.capabilities?.toString())
        }
    }
}