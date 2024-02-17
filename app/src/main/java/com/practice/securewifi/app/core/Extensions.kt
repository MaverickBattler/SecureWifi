package com.practice.securewifi.app.core

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.checkForAccessFineLocationPermission(): Boolean {
    return if (ContextCompat.checkSelfPermission(
            requireActivity().applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_DENIED
    ) {
        // If permission is not yet granted, ask for the permission
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            1
        )
        false
    } else {
        true
    }
}
