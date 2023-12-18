package com.practice.securewifi.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "password_lists", primaryKeys = ["list_name", "password"])
data class PasswordListPasswordCrossRef(
    @ColumnInfo(name = "list_name")
    val listName: String,
    @ColumnInfo(name = "password")
    val password: String
)