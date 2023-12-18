package com.practice.securewifi.check_results.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practice.securewifi.check_results.model.DisplayWifiCheckResult
import com.practice.securewifi.check_results.adapter.CheckResultAdapter
import com.practice.securewifi.data.dao.TriedPasswordsDao
import com.practice.securewifi.data.dao.WifiCheckResultDao
import com.practice.securewifi.databinding.FragmentResultsBinding
import com.practice.securewifi.data.database.WifiSafetyDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResultsFragment : Fragment() {

    private lateinit var wifiCheckResultDao: WifiCheckResultDao

    private lateinit var triedPasswordsDao: TriedPasswordsDao

    private var _binding: FragmentResultsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultsBinding.inflate(inflater, container, false)

        wifiCheckResultDao = WifiSafetyDatabase.getInstance(requireActivity().application).wifiCheckResultDao
        triedPasswordsDao = WifiSafetyDatabase.getInstance(requireActivity().application).triedPasswordsDao

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
        requireActivity().lifecycleScope.launch(Dispatchers.IO) {
            val wifiList = wifiCheckResultDao.getAllWifiCheckResults()
            val wifiListToDisplay = wifiList.map {
                DisplayWifiCheckResult(
                    it.ssid,
                    it.correctPassword,
                    triedPasswordsDao.getTriedPasswordsCountForWifi(it.ssid)
                )
            }
            withContext(Dispatchers.Main) {
                adapter.submitList(wifiListToDisplay)
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }
}