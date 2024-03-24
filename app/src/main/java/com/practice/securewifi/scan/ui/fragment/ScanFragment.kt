package com.practice.securewifi.scan.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practice.securewifi.R
import com.practice.securewifi.app.core.checkForAccessFineLocationPermission
import com.practice.securewifi.app.core.launchOnStarted
import com.practice.securewifi.databinding.FragmentScanBinding
import com.practice.securewifi.scan.model.ScanResultInfo
import com.practice.securewifi.scan.ui.adapter.ScanResultAdapter
import com.practice.securewifi.scan.viewmodel.WifiPointsScanViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ScanFragment : Fragment() {

    private val viewModel by viewModel<WifiPointsScanViewModel>()

    private lateinit var scanResultAdapter: ScanResultAdapter

    private var _binding: FragmentScanBinding? = null

    private val binding get() = _binding!!

    private var openWifiInfoOnCommandJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initObservers()
        val onWifiItemClickListener: (String) -> Unit = { wifiSsid ->
            viewModel.onWifiItemClick(wifiSsid)
        }
        scanResultAdapter = ScanResultAdapter(onWifiItemClickListener)
        binding.recyclerviewScan.adapter = scanResultAdapter

        setupMenu()

        binding.swiperefresh.setOnRefreshListener {
            refresh()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        refresh()
        super.onStart()
    }

    private fun initObservers() {
        viewModel.scanResultInfo.onEach { scanResultInfo ->
            when (scanResultInfo) {
                is ScanResultInfo.ScanSuccess -> {
                    binding.progressBar.isVisible = false
                    binding.scanResultsTextview.isVisible = false
                    scanResultAdapter.submitList(scanResultInfo.scanResults)
                    binding.swiperefresh.isRefreshing = false
                }

                is ScanResultInfo.ScanFailure -> {
                    binding.progressBar.isVisible = false
                    if (scanResultInfo.oldScanResults.isNotEmpty()) {
                        binding.scanResultsTextview.isVisible = false
                        scanResultAdapter.submitList(scanResultInfo.oldScanResults)
                    } else {
                        binding.scanResultsTextview.isVisible = true
                        binding.scanResultsTextview.text = getString(R.string.scan_failure)
                    }
                    binding.swiperefresh.isRefreshing = false
                }

                is ScanResultInfo.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }.launchOnStarted(lifecycleScope)
        openWifiInfoOnCommandJob?.cancel()
        openWifiInfoOnCommandJob = lifecycleScope.launch(Dispatchers.Main) {
            viewModel.openWifiInfoEvent.collect {
                findNavController().navigate(R.id.action_scanFragment_to_wifiInfoFragment)
            }
        }
    }

    private fun refresh() {
        if (checkForAccessFineLocationPermission()) {
            viewModel.onRefresh()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_refresh, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_refresh -> {
                        binding.swiperefresh.isRefreshing = true
                        refresh()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.STARTED)
    }
}