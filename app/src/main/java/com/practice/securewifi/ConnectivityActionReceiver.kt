package com.practice.securewifi

import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.widget.Toast
import android.net.ConnectivityManager


class ConnectivityActionReceiver(private val mListener: OnSampleReadyListener) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent) {
        context?.let {
            if (intent.action == WifiManager.NETWORK_STATE_CHANGED_ACTION) {
                val networkInfo = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
                if (networkInfo!!.isConnected) {
                    // Wifi is connected
                    //System.currentTimeMillis()
                    mListener.onSampleDataReady()
                    //Toast.makeText(context, "Wifi connected" + System.currentTimeMillis()%10000, Toast.LENGTH_SHORT).show()
                } else {
                    //Toast.makeText(context, "Wifi disconnected"+ System.currentTimeMillis()%10000, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    interface OnSampleReadyListener {
        fun onSampleDataReady()
    }
}