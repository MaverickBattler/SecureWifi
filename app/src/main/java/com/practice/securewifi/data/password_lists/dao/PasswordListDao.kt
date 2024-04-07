package com.practice.securewifi.data.password_lists.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practice.securewifi.data.password_lists.entity.PasswordList
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordListDao {

    @Query("SELECT * FROM password_list WHERE list_name = :listName")
    suspend fun getPasswordList(listName: String): PasswordList?

    @Query("SELECT * FROM password_list")
    fun getPasswordListsAsFlow(): Flow<List<PasswordList>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPasswordList(passwordList: PasswordList)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPasswordLists(passwordLists: List<PasswordList>)

    @Query("SELECT * FROM password_list")
    suspend fun getPasswordLists(): List<PasswordList>

    @Query("DELETE FROM password_list WHERE list_name = :listName")
    suspend fun deletePasswordList(listName: String)
}