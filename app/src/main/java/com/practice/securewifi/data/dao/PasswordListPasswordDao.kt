package com.practice.securewifi.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practice.securewifi.data.entity.PasswordListPasswordCrossRef

@Dao
interface PasswordListPasswordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPasswordsForList(passwordList: List<PasswordListPasswordCrossRef>)

    @Query("SELECT password FROM password_list_password_cr WHERE list_name = :listName")
    suspend fun getPasswordsForList(listName: String): List<String>

    @Query("DELETE FROM password_list_password_cr WHERE list_name = :listName")
    suspend fun deletePasswordsForList(listName: String)
}