package com.practice.securewifi.connect

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
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.practice.securewifi.R
import com.practice.securewifi.data.interactor.AllPasswordListsInteractor
import com.practice.securewifi.databinding.FragmentConnectBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject


class ConnectFragment : Fragment(), UpdateListener {

    private var _binding: FragmentConnectBinding? = null

    private val binding get() = _binding!!

    private var service: ConnectionService? = null

    private var serviceBound = false

    private var mConnection: ServiceConnection? = null

    private val allPasswordListsInteractor by inject<AllPasswordListsInteractor>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConnectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val buttonConnect: SecurityCheckButton = binding.securityCheckButton
        val attackInfoTextView = binding.attackInfo
        mConnection = object : ServiceConnection {

            override fun onServiceConnected(className: ComponentName?, binder: IBinder?) {
                val localBinder = binder as ConnectionService.LocalBinder
                service = localBinder.service
                service?.addListener(this@ConnectFragment)
                val latestData = service?.getLatestData()
                latestData?.first?.let { firstCommand ->
                    onUpdate(firstCommand)
                }
                latestData?.second?.let { secondCommand ->
                    onUpdate(secondCommand)
                }
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
                buttonConnect.setState(SecurityCheckButton.State.PREPARATION)
                requireActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
            } else {
                buttonConnect.setState(SecurityCheckButton.State.INITIAL)
            }

            buttonConnect.setOnClickListener {
                if (checkForAccessFineLocationPermission() && checkForPostNotificationsPermission()) {
                    binding.wifiListSpinner.isEnabled = false
                    buttonConnect.setState(SecurityCheckButton.State.PREPARATION)
                    val intent = Intent(requireActivity(), ConnectionService::class.java)
                    val selectedPasswordList = binding.wifiListSpinner.selectedItem.toString()
                    intent.putExtra(ConnectionService.PASSWORD_LIST_PARAMETER, selectedPasswordList)
                    requireActivity().startService(intent)
                    if (!requireActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE)) {
                        attackInfoTextView.text = getString(R.string.connection_start_failure)
                        binding.wifiListSpinner.isEnabled = true
                        buttonConnect.setState(SecurityCheckButton.State.INITIAL)
                    } else {
                        buttonConnect.setState(SecurityCheckButton.State.PROGRESS)
                    }
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val wifiListsNames = allPasswordListsInteractor.getPasswordListsNames()
            withContext(Dispatchers.Main) {
                binding.wifiListSpinner.adapter = ArrayAdapter(
                    requireActivity(),
                    R.layout.wifi_spinner_item,
                    wifiListsNames
                )
            }
        }

        super.onViewCreated(view, savedInstanceState)
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

    override fun onDestroy() {
        mConnection?.let {
            requireActivity().unbindService(it)
        }
        serviceBound = false
        _binding = null
        super.onDestroy()
    }

    private fun checkForAccessFineLocationPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                requireActivity().applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            // If permission is not yet granted, ask for the permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            false
        } else {
            true
        }
    }

    private fun checkForPostNotificationsPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return if (ContextCompat.checkSelfPermission(
                    requireActivity().applicationContext, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_DENIED
            ) {
                // If permission is not yet granted, ask for the permission
                requestPermissions(
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1
                )
                false
            } else {
                true
            }
        } else {
            return true
        }

    }

    override fun onUpdate(command: Command) {
        when (command) {
            is Command.StopConnections -> {
                binding.wifiListSpinner.isEnabled = true
                binding.securityCheckButton.setState(SecurityCheckButton.State.INITIAL)
            }

            is Command.PrepareForConnections -> {
                binding.securityCheckButton.setState(SecurityCheckButton.State.PREPARATION)
            }

            is Command.StartConnections -> {
                binding.securityCheckButton.setState(SecurityCheckButton.State.PROGRESS)
            }

            is Command.AskForAccessFineLocationPermission -> {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }

            is Command.ShowMessageToUser -> {
                binding.attackInfo.text = command.message
            }
        }
    }
}