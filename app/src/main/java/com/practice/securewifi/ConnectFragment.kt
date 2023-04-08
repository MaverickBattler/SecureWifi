package com.practice.securewifi

import android.Manifest
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
import kotlinx.coroutines.*

class ConnectFragment : Fragment(), ConnectivityActionReceiver.OnSampleReadyListener {

    private val interval = 2000L

    private var foundPassword = false

    private lateinit var wifiManager: WifiManager

    private var connection: Job = Job()

    //val uiScope = CoroutineScope(Dispatchers.Main + connection)

    //@RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_connect, container, false)
        val broadcastReceiver = ConnectivityActionReceiver(this)
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        requireActivity().registerReceiver(broadcastReceiver, intentFilter)

        wifiManager = WifiManagerProvider.getWifiManager(requireActivity())

        connection.cancel()

        val buttonConnect: Button = view.findViewById(R.id.start_security_check_button)
        val progressBar: ProgressBar = view.findViewById(R.id.attempting_connects_progressbar)
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
                var correctPassword = ""
                var passwordCount = 0
                connection = GlobalScope.launch {
                    if (ContextCompat.checkSelfPermission(
                            requireActivity().applicationContext,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        val textView =
                            requireView().findViewById<TextView>(R.id.start_security_check_textview)

                        foundPassword = false
                        val networkSSID = "suki_privet"

                        val conf = WifiConfiguration()
                        conf.SSID =
                            "\"" + networkSSID + "\"" // Please note the quotes. String should contain ssid in quotes

                        val networkPasswords = getPasswordsList(networkSSID)
                        for (networkPass in networkPasswords) {
                            //text += "Trying password: $networkPass\n"

                            passwordCount++
                            withContext (Dispatchers.Main) {
                                textView.text = getString(
                                    R.string.attempting_to_connect,
                                    networkSSID,
                                    passwordCount
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
                                        break
                                    }
                                }
                            }
                            delay(interval)
                            if (foundPassword) {
                                correctPassword = networkPass
                                break
                            }

                        }
                        withContext (Dispatchers.Main) {
                            if (foundPassword)
                                textView.text = getString(
                                    R.string.password_was_hacked,
                                    networkSSID,
                                    correctPassword,
                                    passwordCount
                                )
                            else
                                textView.text = getString(
                                    R.string.couldnt_find_password,
                                    networkSSID,
                                    passwordCount
                                )
                        }
                    }
                    connection.cancel()
                    buttonConnect.text = getString(R.string.start)
                    progressBar.visibility = View.INVISIBLE
                }
            } else {
                connection.cancel()
                buttonConnect.text = getString(R.string.start)
                progressBar.visibility = View.INVISIBLE
            }
        }
        return view
    }

    private fun getPasswordsList(ssid: String): List<String> {
        val fixedPasswords = requireActivity().assets.open("passwords.txt").bufferedReader().use {
            it.readLines()
        }
        return listOf(ssid, "pokasuki", "pokasuki69") + fixedPasswords
    }

    override fun onSampleDataReady() {
        foundPassword = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        connection.cancel()
    }
}