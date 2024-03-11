package com.practice.securewifi.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practice.securewifi.data.entity.SelectedWifi
import kotlinx.coroutines.flow.Flow

@Dao
interface SelectedWifiDao {

    @Query("SELECT * FROM selected_wifi WHERE ssid = :ssid")
    suspend fun getSelectedWifi(ssid: String): SelectedWifi

    @Query("SELECT * FROM selected_wifi")
    fun getSelectedWifiesListAsFlow(): Flow<List<SelectedWifi>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSelectedWifi(selectedWifi: SelectedWifi)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSelectedWifies(selectedWifies: List<SelectedWifi>)

    @Query("DELETE FROM selected_wifi WHERE ssid = :selectedWifiSsid")
    suspend fun deleteSelectedWifi(selectedWifiSsid: String)

    @Query("DELETE FROM selected_wifi")
    suspend fun deleteAllSelectedWifies()
}