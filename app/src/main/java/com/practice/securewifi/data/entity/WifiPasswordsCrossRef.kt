package com.practice.securewifi.data.entity

import androidx.room.Entity

@Entity(tableName = "tried_passwords", primaryKeys = ["ssid", "password"])
data class WifiPasswordsCrossRef(
    val ssid: String,
    val password: String
)