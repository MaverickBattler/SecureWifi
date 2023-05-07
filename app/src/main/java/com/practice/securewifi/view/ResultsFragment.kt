package com.practice.securewifi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practice.securewifi.adapter.CheckResultAdapter
import com.practice.securewifi.dao.WifiSafetyDao
import com.practice.securewifi.databinding.FragmentResultsBinding
import com.practice.securewifi.db.WifiSafetyDatabase
import com.practice.securewifi.domain.display.DisplayWifiCheckResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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
        GlobalScope.launch(Dispatchers.IO) {
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