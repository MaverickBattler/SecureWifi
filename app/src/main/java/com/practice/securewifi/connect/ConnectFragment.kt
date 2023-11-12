package com.practice.securewifi.connect

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.practice.securewifi.R
import com.practice.securewifi.databinding.FragmentConnectBinding
import timber.log.Timber


class ConnectFragment : Fragment(), UpdateListener {

    private var _binding: FragmentConnectBinding? = null

    private val binding get() = _binding!!

    private var service: ConnectionService? = null

    private var serviceBound = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConnectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val buttonConnect: SecurityCheckButton = binding.securityCheckButton
        val securityCheckTextView = binding.securityCheckTextview
        askForWifiEnabled()
        val mConnection: ServiceConnection = object : ServiceConnection {

            override fun onServiceConnected(className: ComponentName?, binder: IBinder?) {
                val localBinder = binder as ConnectionService.LocalBinder
                service = localBinder.service
                service?.addListener(this@ConnectFragment)
                serviceBound = true
            }

            override fun onServiceDisconnected(className: ComponentName?) {
                serviceBound = false
            }
        }
        buttonConnect.setState(SecurityCheckButton.State.INITIAL)
        buttonConnect.setOnClickListener {
            //Check if ACCESS_FINE_LOCATION permission is granted
            if (ContextCompat.checkSelfPermission(
                    requireActivity().applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_DENIED
            ) {
                //Ask for the permission
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            } else {
                buttonConnect.setState(SecurityCheckButton.State.PREPARATION)

                val intent = Intent(requireActivity(), ConnectionService::class.java)
                requireActivity().startService(intent)
                if (!requireActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE)) {
                    securityCheckTextView.text = getString(R.string.connection_start_failure)
                } else {
                    buttonConnect.setState(SecurityCheckButton.State.PROGRESS)
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        serviceBound = false
        super.onDestroy()
    }

    private fun askForWifiEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // At this point it might be a good idea to ask the user to enable his wi-fi
            val panelIntent = Intent(Settings.Panel.ACTION_WIFI)
            ActivityCompat.startActivityForResult(requireActivity(), panelIntent, 0, null)
        }
    }

    override fun onUpdate(command: Command) {
        when (command) {
            is Command.StopConnections -> {
                Timber.i("abcdea StopConnections")
                binding.securityCheckButton.setState(SecurityCheckButton.State.INITIAL)
            }
            is Command.PrepareForConnections -> {
                Timber.i("abcdea PrepareForConnections")
                binding.securityCheckButton.setState(SecurityCheckButton.State.PREPARATION)
            }
            is Command.StartConnections -> {
                Timber.i("abcdea StartConnections")
                binding.securityCheckButton.setState(SecurityCheckButton.State.PROGRESS)
            }
            is Command.AskForAccessFineLocationPermission -> {
                Timber.i("abcdea AskForAccessFineLocationPermission")
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
            is Command.ShowMessageToUser -> {
                Timber.i("abcdea ShowMessageToUser: " + command.message)
                binding.securityCheckTextview.text = command.message
            }
        }
    }
}