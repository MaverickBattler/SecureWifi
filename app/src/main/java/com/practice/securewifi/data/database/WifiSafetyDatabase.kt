package com.practice.securewifi.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.practice.securewifi.data.dao.PasswordListsDao
import com.practice.securewifi.data.dao.TriedPasswordsDao
import com.practice.securewifi.data.dao.WifiCheckResultDao
import com.practice.securewifi.data.entity.PasswordListPasswordCrossRef
import com.practice.securewifi.data.entity.WifiCheckResult
import com.practice.securewifi.data.entity.WifiPasswordsCrossRef

@Database(
    entities =
    [
        PasswordListPasswordCrossRef::class,
        WifiCheckResult::class,
        WifiPasswordsCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WifiSafetyDatabase : RoomDatabase() {
    abstract val passwordListsDao: PasswordListsDao
    abstract val triedPasswordsDao: TriedPasswordsDao
    abstract val wifiCheckResultDao: WifiCheckResultDao

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