package com.practice.securewifi.data.password_lists.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practice.securewifi.data.password_lists.entity.PasswordListFixedPassword

@Dao
interface PasswordListFixedPasswordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFixedPasswordsForList(passwordList: List<PasswordListFixedPassword>)

    @Query("SELECT password FROM password_list_fixed_password WHERE list_name = :listName")
    suspend fun getFixedPasswordsForList(listName: String): List<String>

    @Query("DELETE FROM password_list_fixed_password WHERE list_name = :listName")
    suspend fun deleteFixedPasswordsForList(listName: String)
}