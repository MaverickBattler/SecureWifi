package com.practice.securewifi.check.ui

import android.Manifest
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.practice.securewifi.app.core.checkForAccessFineLocationPermission
import com.practice.securewifi.app.core.launchOnStarted
import com.practice.securewifi.app.core.util.Colors
import com.practice.securewifi.check.service.ConnectionService
import com.practice.securewifi.check.passwords_lists_selection.ui.PasswordsListsSelectionDialog
import com.practice.securewifi.check.viewmodel.ConnectViewModel
import com.practice.securewifi.check.wifi_points_selection.ui.WifiPointsSelectionDialog
import com.practice.securewifi.databinding.FragmentConnectBinding
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConnectFragment : Fragment() {

    private val viewModel by viewModel<ConnectViewModel>()

    private var _binding: FragmentConnectBinding? = null

    private val binding get() = _binding!!

    private var mConnection: ServiceConnection? = null

    private var serviceBound = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConnectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        startOrBindConnectService()
        setClickListeners()
        initObservers()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun startOrBindConnectService() {
        mConnection = object : ServiceConnection {

            override fun onServiceConnected(className: ComponentName?, binder: IBinder?) {
                val localBinder = binder as ConnectionService.LocalBinder
                val service = localBinder.service
                service.addListener(viewModel)
                val latestData = service.getLatestData()
                viewModel.onRetrieveLatestDataFromService(latestData)
                serviceBound = true
            }

            override fun onServiceDisconnected(className: ComponentName?) {
                serviceBound = false
            }
        }

        mConnection?.let { mConnection ->
            // if service is already running
            if (isMyServiceRunning(ConnectionService::class.java)) {
                val intent = Intent(requireActivity(), ConnectionService::class.java)
                viewModel.onRebindToService()
                requireActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
            } else {
                viewModel.onSetInitialState()
            }
        }
    }

    private fun initObservers() {
        viewModel.selectedWifiesPreviewUiState.onEach { selectedWifiesPreviewUiState ->
            binding.selectedWifiesTextview.text = selectedWifiesPreviewUiState.text
            val textColor = Colors.getThemeColorFromAttr(
                selectedWifiesPreviewUiState.textColor,
                requireActivity()
            )
            textColor?.let {
                binding.selectedWifiesTextview.setTextColor(textColor)
            }
        }.launchOnStarted(lifecycleScope)
        viewModel.selectedPasswordListsPreviewUiState.onEach { selectedPasswordListsPreviewUiState ->
            binding.selectedPasswordsListsTextview.text =
                selectedPasswordListsPreviewUiState.listsText
            val textColor = Colors.getThemeColorFromAttr(
                selectedPasswordListsPreviewUiState.listsTextColor,
                requireActivity()
            )
            if (selectedPasswordListsPreviewUiState.totalPasswordsAmtString != null) {
                binding.passwordsAmt.isVisible = true
                binding.passwordsAmt.text =
                    selectedPasswordListsPreviewUiState.totalPasswordsAmtString
            } else {
                binding.passwordsAmt.isVisible = false
            }
            textColor?.let {
                binding.selectedPasswordsListsTextview.setTextColor(textColor)
            }
        }.launchOnStarted(lifecycleScope)
        viewModel.securityCheckButtonState.onEach { securityCheckButtonState ->
            binding.securityCheckButton.setState(securityCheckButtonState)
        }.launchOnStarted(lifecycleScope)
        viewModel.attackInfoText.onEach { attackInfoText ->
            binding.attackInfo.text = attackInfoText
        }.launchOnStarted(lifecycleScope)
    }

    private fun setClickListeners() {
        binding.passwordsListsLayout.setOnClickListener {
            PasswordsListsSelectionDialog().show(
                parentFragmentManager,
                PasswordsListsSelectionDialog.TAG
            )
        }
        binding.listOfWifiesLayout.setOnClickListener {
            WifiPointsSelectionDialog().show(
                parentFragmentManager,
                WifiPointsSelectionDialog.TAG
            )
        }
        binding.securityCheckButton.setOnClickListener {
            if (checkForAccessFineLocationPermission() && checkForPostNotificationsPermission()) {
                mConnection?.let { mConnection ->
                    viewModel.onStartCheck()
                    val intent = Intent(requireActivity(), ConnectionService::class.java)
                    requireActivity().startService(intent)
                    if (!requireActivity().bindService(
                            intent,
                            mConnection,
                            Context.BIND_AUTO_CREATE
                        )
                    ) {
                        viewModel.onBindToService(false)
                    } else {
                        viewModel.onBindToService(true)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        if (serviceBound) {
            mConnection?.let { serviceConnection ->
                requireActivity().unbindService(serviceConnection)
            }
        }
        serviceBound = false
        _binding = null
        super.onDestroy()
    }

    private fun checkForPostNotificationsPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireActivity().applicationContext, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_DENIED
            ) {
                // If permission is not yet granted, ask for the permission
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
                false
            } else {
                true
            }
        } else {
            true
        }
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager =
            requireActivity().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        for (service in manager!!.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}