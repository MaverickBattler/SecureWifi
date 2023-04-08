package com.practice.securewifi

import android.content.Context
import android.net.wifi.WifiManager

object WifiManagerProvider {
    fun getWifiManager(context: Context) =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
}