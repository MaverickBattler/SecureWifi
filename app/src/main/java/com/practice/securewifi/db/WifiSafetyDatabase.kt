package com.practice.securewifi.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.practice.securewifi.dao.WifiSafetyDao
import com.practice.securewifi.domain.entity.WifiCheckResult
import com.practice.securewifi.domain.entity.WifiPasswordsCrossRef

@Database(
    entities = [WifiCheckResult::class, WifiPasswordsCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class WifiSafetyDatabase : RoomDatabase() {
    abstract val wifiSafetyDao: WifiSafetyDao

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