package com.practice.securewifi.data.password_lists.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "password_list_fixed_password", primaryKeys = ["list_name", "password"])
data class PasswordListFixedPassword(
    @ColumnInfo(name = "list_name")
    val listName: String,
    @ColumnInfo(name = "password")
    val password: String
)