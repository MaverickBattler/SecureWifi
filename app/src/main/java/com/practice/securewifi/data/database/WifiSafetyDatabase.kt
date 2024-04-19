package com.practice.securewifi.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.practice.securewifi.data.password_lists.dao.PasswordListDao
import com.practice.securewifi.data.password_lists.dao.PasswordListFixedPasswordDao
import com.practice.securewifi.data.dao.SelectedWifiDao
import com.practice.securewifi.data.dao.TriedPasswordsDao
import com.practice.securewifi.data.dao.WifiCheckResultDao
import com.practice.securewifi.data.password_lists.entity.PasswordList
import com.practice.securewifi.data.password_lists.entity.PasswordListFixedPassword
import com.practice.securewifi.data.entity.SelectedWifi
import com.practice.securewifi.data.entity.WifiCheckResult
import com.practice.securewifi.data.entity.WifiPasswordsCrossRef
import com.practice.securewifi.data.password_lists.dao.PersonInfoDao
import com.practice.securewifi.data.password_lists.dao.PlaceNameDao
import com.practice.securewifi.data.password_lists.entity.PersonInfo
import com.practice.securewifi.data.password_lists.entity.PlaceName

@Database(
    entities =
    [
        PasswordListFixedPassword::class,
        WifiCheckResult::class,
        WifiPasswordsCrossRef::class,
        PasswordList::class,
        SelectedWifi::class,
        PersonInfo::class,
        PlaceName::class,
    ],
    version = 2,
    exportSchema = false
)
abstract class WifiSafetyDatabase : RoomDatabase() {
    abstract val passwordListFixedPasswordDao: PasswordListFixedPasswordDao
    abstract val passwordListDao: PasswordListDao
    abstract val triedPasswordsDao: TriedPasswordsDao
    abstract val wifiCheckResultDao: WifiCheckResultDao
    abstract val selectedWifiDao: SelectedWifiDao
    abstract val personInfoDao: PersonInfoDao
    abstract val placeNameDao: PlaceNameDao

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