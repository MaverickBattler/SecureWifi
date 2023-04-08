package com.practice.securewifi

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.WifiConfiguration
import android.os.Build
import android.os.IBinder
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay

class ConnectionService : Service() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // If the notification supports a direct reply action, use
        // PendingIntent.FLAG_MUTABLE instead.
        val pendingIntent: PendingIntent =
            Intent(this, ConnectFragment::class.java).let { notificationIntent ->
                PendingIntent.getActivity(
                    this, 0, notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }

        val notification: Notification = Notification.Builder(
            this,
            "SecureWifiConnectionAttempts"
        )
            .setContentTitle(getText(R.string.notification_title))
            .setContentText(getText(R.string.notification_message))
            .setSmallIcon(R.drawable.wifi_password)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(23, notification)

        /*if (ContextCompat.checkSelfPermission(
                requireActivity().applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            var text = ""
            val textView =
                requireView().findViewById<TextView>(R.id.start_security_check_textview)
            var correctPassword = ""

            foundPassword = false
            val networkSSID = "suki_privet"

            requireActivity().runOnUiThread {
                textView.text = getString(R.string.attempting_to_connect)
            }

            val conf = WifiConfiguration()
            conf.SSID =
                "\"" + networkSSID + "\"" // Please note the quotes. String should contain ssid in quotes

            val networkPasswords = getPasswordsList(networkSSID)
            for (networkPass in networkPasswords) {
                //text += "Trying password: $networkPass" + "\n"
                *//*requireActivity().runOnUiThread {
                    Toast.makeText(
                        requireActivity(),
                        "Starting to connect with password: $networkPass " + System.currentTimeMillis() % 10000,
                        Toast.LENGTH_SHORT
                    ).show()

                }*//*

                conf.preSharedKey = "\"" + networkPass + "\""
                // only works for API < 29
                val netId = wifiManager.addNetwork(conf)
                text += netId.toString() + "\n"
                if (netId != -1) {
                    text += "Disconnect from wifi: " + wifiManager.disconnect() + "\n"
                    text += "Enable network: " + wifiManager.enableNetwork(
                        netId,
                        true
                    ) + "\n"
                } else {
                    val list = wifiManager.configuredNetworks
                    for (network in list) {
                        if (network.SSID != null && network.SSID == "\"" + networkSSID + "\"") {
                            text += "Found network with ID = " + network.networkId + "\n"
                            text += "Disconnect from wifi: " + wifiManager.disconnect() + "\n"
                            text += "Enable network: " + wifiManager.enableNetwork(
                                network.networkId,
                                true
                            ) + "\n"
                            break
                        }
                    }
                }
                requireActivity().runOnUiThread {
                    textView.text = text
                }
                delay(interval)
                if (foundPassword) {
                    correctPassword = networkPass
                    requireActivity().runOnUiThread {
                        text =
                            "Password was hacked! The correct password was $correctPassword!"
                    }
                    break
                }

            }
            if (!foundPassword)
                text = "Coulnd't find a password for network $networkSSID"
            textView.text = text
        }*/
        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

}