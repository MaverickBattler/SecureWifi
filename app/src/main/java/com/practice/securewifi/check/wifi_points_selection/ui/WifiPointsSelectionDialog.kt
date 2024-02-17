package com.practice.securewifi.check.wifi_points_selection.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.practice.securewifi.app.core.base.BaseDialogFragment
import com.practice.securewifi.databinding.FragmentWifiInfoBinding

class WifiPointsSelectionDialog : BaseDialogFragment() {

    private var _binding: FragmentWifiInfoBinding? = null

    private val binding get() = _binding!!

    override val dialogWidth: Float = 85f
    override val dialogHeight: Float = 85f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWifiInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}