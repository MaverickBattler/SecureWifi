package com.practice.securewifi.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practice.securewifi.data.entity.WifiPasswordsCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface TriedPasswordsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWifiPasswordsCrossRef(wifiPasswordsCrossRef: WifiPasswordsCrossRef)

    @Query("SELECT COUNT(password) FROM tried_passwords GROUP BY ssid")
    fun getTriedPasswordsCountAsFlow(): Flow<List<Int>>

    @Query("SELECT COUNT(password) FROM tried_passwords WHERE ssid = :ssid")
    suspend fun getTriedPasswordsCountForWifi(ssid: String): Int

    @Query("SELECT password FROM tried_passwords WHERE ssid = :ssid")
    fun getTriedPasswordsForWifiAsFlow(ssid: String): Flow<List<String>>

    @Query("SELECT password FROM tried_passwords WHERE ssid = :ssid")
    fun getTriedPasswordsForWifi(ssid: String): List<String>
}