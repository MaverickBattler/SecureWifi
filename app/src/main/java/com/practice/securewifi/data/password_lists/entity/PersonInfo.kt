package com.practice.securewifi.data.password_lists.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "person_info")
data class PersonInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "list_name")
    val listName: String,
    val name: String? = null,
    @ColumnInfo(name = "second_name")
    val secondName: String? = null,
    @ColumnInfo(name = "father_or_middle_name")
    val fatherOrMiddleName: String? = null,
    val day: Int? = null,
    val month: Int? = null,
    val year: Int? = null
)