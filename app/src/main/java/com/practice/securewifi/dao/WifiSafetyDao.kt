package com.practice.securewifi.dao

import androidx.room.*
import com.practice.securewifi.domain.entity.WifiCheckResult
import com.practice.securewifi.domain.entity.WifiPasswordsCrossRef

@Dao
interface WifiSafetyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWifiCheckResult(wifiCheckResult: WifiCheckResult)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWifiPasswordsCrossRef(wifiPasswordsCrossRef: WifiPasswordsCrossRef)

    @Query("SELECT * FROM wifi_check_result WHERE ssid = :ssid")
    suspend fun getWifiCheckResult(ssid: String): WifiCheckResult?

    @Query("SELECT password FROM tried_passwords WHERE ssid = :ssid")
    suspend fun getTriedPasswordsForWifi(ssid: String): List<String>

    @Query("SELECT * FROM wifi_check_result")
    suspend fun getAllWifiCheckResults(): List<WifiCheckResult>

    @Query("SELECT COUNT(password) FROM tried_passwords WHERE ssid = :ssid")
    suspend fun getTriedPasswordsCountForWifi(ssid: String): Int
}