package com.practice.securewifi.data.password_lists.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "place_name")
data class PlaceName(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "list_name")
    val listName: String,
    @ColumnInfo(name = "place_name")
    val placeName: String,
)