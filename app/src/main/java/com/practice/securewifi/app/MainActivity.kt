package com.practice.securewifi.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.practice.securewifi.R
import com.practice.securewifi.connect.ConnectFragment
import com.practice.securewifi.check_results.ResultsFragment
import com.practice.securewifi.scan.ScanFragment

class MainActivity : AppCompatActivity() {

    private val connectFragment = ConnectFragment()
    private var scanFragment = ScanFragment()
    private var resultsFragment = ResultsFragment()
    private val fragmentManager = supportFragmentManager
    private var activeFragment: Fragment = connectFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Adding all fragments and hiding all but one
        addAllFragments()

        // Setting up the app bar
        setUpSupportBar()

        // Setting up the bottom navigation view
        setUpBottomNavigationView()

        //Check if ACCESS_FINE_LOCATION permission is granted
        checkAccessFineLocationPermission()

        // At this point it might be a good idea to ask the user to enable his wi-fi
        askForWifiEnabled()
    }

    private fun addAllFragments() {
        supportFragmentManager.beginTransaction().apply {
            add(R.id.nav_host_fragment, connectFragment, "connect_fragment")
            add(R.id.nav_host_fragment, scanFragment, "scan_fragment").hide(scanFragment)
            add(R.id.nav_host_fragment, resultsFragment, "results_fragment").hide(resultsFragment)
        }.commit()
    }

    private fun setUpSupportBar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    private fun setUpBottomNavigationView() {
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.connectFragment -> {
                    handleConnectMenuItemClick()
                    true
                }
                R.id.scanFragment -> {
                    handleScanMenuItemClick()
                    true
                }
                R.id.resultsFragment -> {
                    handleResultsMenuItemClick()
                    true
                }
                else -> false
            }
        }
    }

    private fun handleConnectMenuItemClick() {
        supportActionBar?.title = getString(R.string.check_of_passwords_safety)
        fragmentManager.beginTransaction().hide(activeFragment)
            .show(connectFragment).commit()
        activeFragment = connectFragment
    }
    private fun handleScanMenuItemClick() {
        supportActionBar?.title = getString(R.string.list_of_wifi_nearby)
        fragmentManager.beginTransaction().hide(activeFragment)
            .show(scanFragment).commit()
        activeFragment = scanFragment
    }
    private fun handleResultsMenuItemClick() {
        supportActionBar?.title = getString(R.string.security_check_results)
        val prevFragment = resultsFragment
        resultsFragment = ResultsFragment()
        // ResultsFragment is recreated every time the menu item is clicked
        fragmentManager.beginTransaction().remove(prevFragment)
            .add(R.id.nav_host_fragment, resultsFragment).hide(activeFragment)
            .show(resultsFragment).commit()
        activeFragment = resultsFragment
    }

    private fun checkAccessFineLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            //Ask for the permission
            ActivityCompat.requestPermissions(
                this@MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
        }
    }

    private fun askForWifiEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val panelIntent = Intent(Settings.Panel.ACTION_WIFI)
            ActivityCompat.startActivityForResult(this, panelIntent, 0, null)
        }
    }
}

