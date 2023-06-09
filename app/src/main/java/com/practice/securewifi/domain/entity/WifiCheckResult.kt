package com.practice.securewifi.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wifi_check_result")
data class WifiCheckResult(
    @PrimaryKey
    val ssid: String,
    val correctPassword: String?
)