package com.practice.securewifi.check.service

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.wifi.ScanResult
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.*
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.practice.securewifi.R
import com.practice.securewifi.app.MainActivity
import com.practice.securewifi.check.Command
import com.practice.securewifi.check.receiver.ConnectivityActionReceiver
import com.practice.securewifi.check.UpdateListener
import com.practice.securewifi.check.interactor.TriedPasswordsInteractor
import com.practice.securewifi.check.interactor.WifiCheckResultInteractor
import com.practice.securewifi.data.entity.WifiCheckResult
import com.practice.securewifi.core.util.WifiManagerProvider
import com.practice.securewifi.check.interactor.PasswordListsInteractor
import com.practice.securewifi.check.interactor.SelectedWifiesInteractor
import com.practice.securewifi.scan_feature.WifiScanManager
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class ConnectionService : Service(), ConnectivityActionReceiver.OnSampleReadyListener {

    private var foundPassword = false

    private var connection: Job = Job()

    private lateinit var wifiManager: WifiManager

    private var wifiScanManager: WifiScanManager? = null

    private lateinit var connectivityActionReceiver: ConnectivityActionReceiver

    private val triedPasswordsInteractor: TriedPasswordsInteractor by inject()

    private val wifiCheckResultInteractor: WifiCheckResultInteractor by inject()

    private val selectedWifiesInteractor: SelectedWifiesInteractor by inject()

    private lateinit var binder: LocalBinder

    private lateinit var notificationChannel: String

    private var updateListener: UpdateListener? = null

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private var timeoutJob: Job = Job()

    private var latestCommand: Command = Command.StartConnections
    private var latestMessage: Command? = null

    private val passwordListsInteractor by inject<PasswordListsInteractor>()

    inner class LocalBinder : Binder() {
        val service: ConnectionService = this@ConnectionService
    }

    fun addListener(listener: UpdateListener) {
        updateListener = listener
    }

    fun getLatestData(): Pair<Command, Command?> {
        return Pair(latestCommand, latestMessage)
    }

    override fun onCreate() {
        connectivityActionReceiver = ConnectivityActionReceiver(this)
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        registerReceiver(connectivityActionReceiver, intentFilter)

        wifiManager = WifiManagerProvider.getWifiManager(applicationContext)

        binder = LocalBinder()

        connection.cancel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!connection.isActive) {
            createNotification()
            wifiManager.isWifiEnabled = true
            update(Command.ShowMessageToUser(getString(R.string.starting_scan)))
            update(Command.PrepareForConnections)
            startScanningForNearbyWifi()
        } else {
            wifiScanManager?.stop()
            stopConnections()
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        unregisterReceiver(connectivityActionReceiver)
        wifiScanManager?.stop()
        super.onDestroy()
    }

    override fun onRebind(intent: Intent?) {
        update(latestCommand)
        latestMessage?.let {
            update(it)
        }
        super.onRebind(intent)
    }

    override fun onBind(intent: Intent?): IBinder {
        update(latestCommand)
        latestMessage?.let {
            update(it)
        }
        return binder
    }

    private fun createNotification() {
        val intent = Intent(this, MainActivity::class.java)
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        notificationChannel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel("my_service", "My Background Service")
        } else {
            // If earlier version channel ID is not used
            // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
            ""
        }

        val notification = NotificationCompat.Builder(
            this,
            notificationChannel
        )
            .setContentTitle(getText(R.string.performing_connections))
            .setContentText(getText(R.string.starting_scan))
            .setSmallIcon(R.drawable.wifi_password)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(PERSISTENT_NOTIFICATION_ID, notification)
    }

    private fun updateNotification(contentTitle: CharSequence, contentText: CharSequence) {
        val intent = Intent(this, MainActivity::class.java)
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification = NotificationCompat.Builder(
            this,
            notificationChannel
        )
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setSmallIcon(R.drawable.wifi_password)
            .setContentIntent(pendingIntent)
            .build()

        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(PERSISTENT_NOTIFICATION_ID, notification)
    }

    private fun showNotification(contentTitle: CharSequence?, contentText: CharSequence) {
        val intent = Intent(this, MainActivity::class.java)
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification = NotificationCompat.Builder(
            this,
            notificationChannel
        )
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setSmallIcon(R.drawable.wifi_password)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .build()
        with(NotificationManagerCompat.from(this)) {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // notificationId is a unique int for each notification
                notify(DISMISSABLE_NOTIFICATION_ID, notification)
            }
        }
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

        wifiScanManager = object : WifiScanManager(this@ConnectionService) {
            override fun onScanSuccess(scanResults: List<ScanResult>) {
                wifiScanManager?.stop()
                scanSuccess(scanResults)
            }

            override fun onScanFailure(oldScanResults: List<ScanResult>) {
                wifiScanManager?.stop()
                if (oldScanResults.isEmpty()) {
                    scanFailure()
                } else {
                    scanSuccess(oldScanResults)
                }
            }
        }
        wifiScanManager?.startScan()

        // Timeout if connections didn't start in 5 seconds
        timeoutJob.cancel()
        timeoutJob = coroutineScope.launch(Dispatchers.IO) {
            delay(START_CONNECTIONS_TIMEOUT)
            wifiScanManager?.stop()
            if (!connection.isActive) {
                stopConnections()
            }
        }
    }

    private fun scanSuccess(nearbyWifiPoints: List<ScanResult>) {
        connection = coroutineScope.launch(Dispatchers.IO) {
            val wifiSSIDs = getWifiSsids(nearbyWifiPoints)
            attemptConnections(wifiSSIDs)
        }
    }

    private suspend fun getWifiSsids(nearbyWifiPoints: List<ScanResult>): List<String> {
        return nearbyWifiPoints
            .filter {
                WifiManager.calculateSignalLevel(
                    it.level,
                    10
                ) > 6
            }
            .sortedByDescending { scanResult ->
                scanResult.level
            }
            .map { it.SSID }
            .filter { ssid -> // filter only selected wifies out of wifies nearby
                selectedWifiesInteractor.getSelectedWifiesSsids().contains(ssid)
            }
    }

    private suspend fun attemptConnections(wifiSSIDs: List<String>) {
        var correctPassword = ""
        var passwordCountNeeded = 0

        withContext(Dispatchers.Main) {
            update(Command.StartConnections)
        }
        foundPassword = false
        var networkCount = 0
        for (networkSSID in wifiSSIDs) {
            networkCount++
            // If password was found during this search
            if (foundPassword) break

            val wifi = wifiCheckResultInteractor.getWifiCheckResult(networkSSID)
            // If this network hasn't ever been a subject to hacking, save it
            if (wifi == null) {
                wifiCheckResultInteractor.insertWifiCheckResult(WifiCheckResult(networkSSID, null))
            } else if (wifi.correctPassword != null) {
                // If this particular network has already been hacked
                continue
            }
            val triedPasswords = triedPasswordsInteractor.getTriedPasswordsForWifi(networkSSID)

            val conf = WifiConfiguration()
            conf.SSID = "\"" + networkSSID + "\"" // String should contain ssid in quotes

            val networkPasswords = passwordListsInteractor.getPasswordsForSsid(networkSSID)
            var passwordCount = 0

            for (networkPass in networkPasswords) {
                passwordCount++

                // If this password has already been checked for this wifi skip it
                if (triedPasswords.contains(networkPass)) continue

                withContext(Dispatchers.Main) {
                    update(
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
                    updateNotification(
                        getString(R.string.performing_connections),
                        getString(
                            R.string.attempting_to_connect_notification,
                            networkSSID,
                            networkCount, // count of networks already checked
                            wifiSSIDs.size, // count of networks need to be checked
                            networkPass, // password currently being tried
                            passwordCount, // count of passwords already tried
                            networkPasswords.size // password count for wifi
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
                    try {
                        val list = wifiManager.configuredNetworks
                        for (network in list) {
                            if (network.SSID != null && network.SSID == "\"" + networkSSID + "\"") {
                                wifiManager.disconnect()
                                wifiManager.enableNetwork(network.networkId, true)
                                wifiManager.reconnect()
                                break
                            }
                        }
                    } catch (e: SecurityException) {
                        Timber.tag(TAG).e(e)
                        onNotGivenPermission()
                    }
                }
                delay(INTERVAL)

                triedPasswordsInteractor.insertAttemptedPasswordForWifi(
                    networkPass,
                    networkSSID,
                )

                if (foundPassword) {
                    correctPassword = networkPass
                    passwordCountNeeded = passwordCount
                    wifiCheckResultInteractor.insertWifiCheckResult(
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
                    update(
                        Command.ShowMessageToUser(
                            getString(
                                R.string.password_hacking_results,
                                networkSSID,
                                correctPassword,
                                passwordCountNeeded
                            )
                        )
                    )
                    showNotification(
                        getString(R.string.connection_attempts_ended_success),
                        getString(
                            R.string.password_hacking_results_notification,
                            networkSSID,
                            correctPassword
                        )
                    )
                }
            }
        }
        if (!foundPassword) {
            withContext(Dispatchers.Main) {
                update(
                    Command.ShowMessageToUser(
                        getString(R.string.couldnt_find_password)
                    )
                )
                showNotification(
                    getString(R.string.connection_attempts_ended_nothing_found),
                    getString(R.string.couldnt_find_password_notification)
                )
            }
        }
        withContext(Dispatchers.Main) {
            stopConnections()
        }
    }

    private fun scanFailure() {
        update(Command.ShowMessageToUser(getString(R.string.scan_failure)))
        stopConnections()
    }

    private fun onNotGivenPermission() {
        stopConnections()
    }

    private fun stopConnections() {
        update(Command.StopConnections)
        connection.cancel()
        stopForegroundService()
    }

    private fun stopForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        }
        stopSelf()
    }

    private fun update(command: Command) {
        updateListener?.onUpdate(command)
        if (command is Command.ShowMessageToUser) {
            latestMessage = command
        } else {
            latestCommand = command
        }
    }

    override fun onSampleDataReady() {
        foundPassword = true
    }

    companion object {

        private const val PERSISTENT_NOTIFICATION_ID = 23

        private const val DISMISSABLE_NOTIFICATION_ID = 1235

        private const val INTERVAL = 3000L

        private const val START_CONNECTIONS_TIMEOUT = 5000L

        val TAG: String = ConnectionService::class.java.simpleName
    }
}