package com.practice.securewifi.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "selected_wifi")
class SelectedWifi(
    @PrimaryKey
    val ssid: String
)