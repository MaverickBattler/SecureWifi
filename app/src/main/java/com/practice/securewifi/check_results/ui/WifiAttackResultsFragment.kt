package com.practice.securewifi.check_results.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.practice.securewifi.R
import com.practice.securewifi.app.core.launchOnStarted
import com.practice.securewifi.check_results.adapter.WifiAttackResultsAdapter
import com.practice.securewifi.check_results.viewmodel.WifiAttackResultsViewModel
import com.practice.securewifi.databinding.FragmentWifiAttackResultsBinding
import com.practice.securewifi.app.core.util.Colors
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WifiAttackResultsFragment : Fragment() {

    private var _binding: FragmentWifiAttackResultsBinding? = null
    private val binding get() = _binding!!

    private val args: WifiAttackResultsFragmentArgs by navArgs()

    private val wifiName by lazy { args.wifiName }

    private val viewModel: WifiAttackResultsViewModel by viewModel { parametersOf(wifiName) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWifiAttackResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = WifiAttackResultsAdapter()
        binding.triedPasswordsList.adapter = adapter
        binding.triedPasswordsList.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.wifiSsidTextview.text = wifiName
        viewModel.triedPasswordList.onEach { triedPassword ->
            adapter.submitList(triedPassword)
        }.launchOnStarted(lifecycleScope)
        viewModel.correctPassword.onEach { correctPassword ->
            val wasWifiHackedTextview = binding.wasWifiHackedTextview
            wasWifiHackedTextview.text = if (correctPassword == null) {
                val colorFromTheme =
                    Colors.getThemeColorFromAttr(R.attr.textColorMain, requireActivity())
                colorFromTheme?.let { color ->
                    wasWifiHackedTextview.setTextColor(color)
                }
                getString(R.string.password_was_not_hacked)
            } else {
                wasWifiHackedTextview.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(), R.color.success_green
                    )
                )
                getString(R.string.correct_password_is, correctPassword)
            }
        }.launchOnStarted(lifecycleScope)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}