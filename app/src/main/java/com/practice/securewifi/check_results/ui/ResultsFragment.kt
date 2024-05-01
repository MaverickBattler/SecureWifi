package com.practice.securewifi.check_results.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practice.securewifi.R
import com.practice.securewifi.core.extensions.collectOnStarted
import com.practice.securewifi.check_results.adapter.CheckResultAdapter
import com.practice.securewifi.check_results.viewmodel.ResultsViewModel
import com.practice.securewifi.databinding.FragmentResultsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResultsFragment : Fragment() {

    private var _binding: FragmentResultsBinding? = null

    private val binding get() = _binding!!

    private val viewModel: ResultsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val onItemClickListener: (String) -> Unit = { wifiSsid ->
            val action = ResultsFragmentDirections.actionResultsFragmentToWifiAttackResultFragment(
                wifiSsid
            )
            findNavController().navigate(action)
        }
        val adapter = CheckResultAdapter(onItemClickListener)
        binding.recyclerviewResults.adapter = adapter
        viewModel.displayWifiCheckResults.collectOnStarted(
            lifecycleScope,
            lifecycle
        ) { listToDisplay ->
            binding.noResultsTextview.isVisible = listToDisplay.isEmpty()
            adapter.submitList(listToDisplay)
        }
        setupMenu()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_delete_all, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_delete_all -> {
                        viewModel.deleteAllResults()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.STARTED)
    }
}