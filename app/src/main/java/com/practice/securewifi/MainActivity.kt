package com.practice.securewifi

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            }*/

        // Setting up the app bar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        // Getting a reference to the NavHostFragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        // Getting a reference to the NavController
        val navController = navHostFragment.navController
        // Make app bar display the label of current fragment
        val builder = AppBarConfiguration.Builder(
            R.id.connectFragment,
            R.id.scanFragment,
            R.id.resultsFragment
        )
        val appBarConfiguration = builder.build()
        toolbar.setupWithNavController(navController, appBarConfiguration)
        // Set the title to the label of the first destination
        supportActionBar?.title = navController.currentDestination?.label
        // Setting up the bottom navigation view
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavView.setupWithNavController(navController)

        //Check if ACCESS_FINE_LOCATION permission is granted
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            //Ask for the permission
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
        }
    }

    /*private fun checkWifiOnAndConnected(): Boolean {
        return if (wifiManager.isWifiEnabled) { // Wi-Fi adapter is ON
            val wifiInfo = wifiManager.connectionInfo
            // Whether connected to an access point
            wifiInfo.networkId != -1
        } else {
            false // Wi-Fi adapter is OFF
        }
    }*/

    /*private fun connectToWifi() {
        val networkSSID = "suki_privet"
        val networkPass = "pokasuki69"

        val conf = WifiConfiguration()
        conf.SSID =
            "\"" + networkSSID + "\"" // Please note the quotes. String should contain ssid in quotes

        conf.preSharedKey = "\"" + networkPass + "\""
        var text = ""
        val netId = wifiManager.addNetwork(conf)
        text += netId.toString() + "\n"
        if (netId != -1) {
            text += "Disconnect from wifi: " + wifiManager.disconnect() + "\n"
            text += "Enable network: " + wifiManager.enableNetwork(netId, true) + "\n"
        } else {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val list = wifiManager.configuredNetworks
                for (i in list) {
                    if (i.SSID != null && i.SSID == "\"" + networkSSID + "\"") {
                        text += "Found network with ID = " + i.networkId + "\n"
                        text += "Disconnect from wifi: " + wifiManager.disconnect() + "\n"
                        text += "Enable network: " + wifiManager.enableNetwork(
                            i.networkId,
                            true
                        ) + "\n"
                        break
                    }
                }
            }
        }

        val textView = findViewById<TextView>(R.id.wifiScanResults)
        textView.text = text

    }*/
}

