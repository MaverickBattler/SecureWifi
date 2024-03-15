package com.practice.securewifi.check_results.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
        viewModel.displayWifiCheckResults.observe(viewLifecycleOwner) { listToDisplay ->
            binding.noResultsTextview.isVisible = listToDisplay.isEmpty()
            adapter.submitList(listToDisplay)
        }
        super.onViewCreated(view, savedInstanceState)
    }
}