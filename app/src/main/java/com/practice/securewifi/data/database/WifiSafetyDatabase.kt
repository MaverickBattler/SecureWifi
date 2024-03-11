package com.practice.securewifi.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.practice.securewifi.data.dao.PasswordListDao
import com.practice.securewifi.data.dao.PasswordListPasswordDao
import com.practice.securewifi.data.dao.SelectedWifiDao
import com.practice.securewifi.data.dao.TriedPasswordsDao
import com.practice.securewifi.data.dao.WifiCheckResultDao
import com.practice.securewifi.data.entity.PasswordList
import com.practice.securewifi.data.entity.PasswordListPasswordCrossRef
import com.practice.securewifi.data.entity.SelectedWifi
import com.practice.securewifi.data.entity.WifiCheckResult
import com.practice.securewifi.data.entity.WifiPasswordsCrossRef

@Database(
    entities =
    [
        PasswordListPasswordCrossRef::class,
        WifiCheckResult::class,
        WifiPasswordsCrossRef::class,
        PasswordList::class,
        SelectedWifi::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WifiSafetyDatabase : RoomDatabase() {
    abstract val passwordListPasswordDao: PasswordListPasswordDao
    abstract val passwordListDao: PasswordListDao
    abstract val triedPasswordsDao: TriedPasswordsDao
    abstract val wifiCheckResultDao: WifiCheckResultDao
    abstract val selectedWifiDao: SelectedWifiDao

    companion object {
        @Volatile
        private var INSTANCE: WifiSafetyDatabase? = null

        fun getInstance(context: Context): WifiSafetyDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        WifiSafetyDatabase::class.java,
                        "wifi_safety_database"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}