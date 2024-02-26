package com.practice.securewifi.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wifi_check_result")
data class WifiCheckResult(
    @PrimaryKey
    val ssid: String,
    @ColumnInfo(name = "correct_password")
    val correctPassword: String?
)