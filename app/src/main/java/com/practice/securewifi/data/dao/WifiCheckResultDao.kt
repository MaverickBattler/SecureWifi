package com.practice.securewifi.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practice.securewifi.data.entity.WifiCheckResult
import kotlinx.coroutines.flow.Flow

@Dao
interface WifiCheckResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWifiCheckResult(wifiCheckResult: WifiCheckResult)

    @Query("SELECT * FROM wifi_check_result")
    fun getAllWifiCheckResults(): Flow<List<WifiCheckResult>>

    @Query("SELECT * FROM wifi_check_result WHERE ssid = :ssid")
    fun getWifiCheckResultAsFlow(ssid: String): Flow<WifiCheckResult?>

    @Query("SELECT * FROM wifi_check_result WHERE ssid = :ssid")
    suspend fun getWifiCheckResult(ssid: String): WifiCheckResult?

    @Query("DELETE FROM wifi_check_result")
    suspend fun deleteAllWifiCheckResults()
}