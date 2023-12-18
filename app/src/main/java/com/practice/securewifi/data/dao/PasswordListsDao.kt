package com.practice.securewifi.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.practice.securewifi.data.entity.PasswordListPasswordCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordListsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPasswordsList(passwordList: List<PasswordListPasswordCrossRef>)

    @Query("SELECT password FROM password_lists WHERE list_name = :listName")
    suspend fun getPasswordsForList(listName: String): List<String>

    @Query("SELECT list_name FROM password_lists GROUP BY list_name")
    fun getPasswordListsAsFlow(): Flow<List<String>>

    @Query("SELECT list_name FROM password_lists GROUP BY list_name")
    suspend fun getPasswordLists(): List<String>

    @Query("DELETE FROM password_lists WHERE list_name = :listName")
    suspend fun deletePasswordList(listName: String)

    @Transaction
    suspend fun saveList(oldListName: String, newListName: String, passwordList: List<String>) {
        deletePasswordList(oldListName)
        insertPasswordsList(passwordList.map { PasswordListPasswordCrossRef(newListName, it) })
    }
}