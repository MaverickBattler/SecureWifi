package com.practice.securewifi.check_results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.practice.securewifi.result_storage.dao.WifiSafetyDao
import com.practice.securewifi.databinding.FragmentResultsBinding
import com.practice.securewifi.result_storage.database.WifiSafetyDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResultsFragment : Fragment() {

    private lateinit var dao: WifiSafetyDao

    private var _binding: FragmentResultsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultsBinding.inflate(inflater, container, false)

        dao = WifiSafetyDatabase.getInstance(requireActivity().application).wifiSafetyDao

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = CheckResultAdapter()
        binding.recyclerviewResults.adapter = adapter
        requireActivity().lifecycleScope.launch(Dispatchers.IO) {
            val wifiList = dao.getAllWifiCheckResults()
            val wifiListToDisplay = wifiList.map {
                DisplayWifiCheckResult(
                    it.ssid,
                    it.correctPassword,
                    dao.getTriedPasswordsCountForWifi(it.ssid)
                )
            }
            withContext(Dispatchers.Main) {
                adapter.submitList(wifiListToDisplay)
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }
}