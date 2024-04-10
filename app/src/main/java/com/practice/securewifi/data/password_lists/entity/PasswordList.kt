package com.practice.securewifi.data.password_lists.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "password_list")
data class PasswordList(
    @PrimaryKey
    @ColumnInfo(name = "list_name")
    val listName: String,
    val deletable: Boolean,
    val selected: Boolean,
    val amountOfGeneratedPasswords: Int
)