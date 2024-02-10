package com.practice.securewifi.app.core

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.askForAccessFineLocationPermissionIfNeeded() {
    if (ContextCompat.checkSelfPermission(
            requireActivity().applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_DENIED
    ) {
        //Ask for the permission
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
        )
    }
}
