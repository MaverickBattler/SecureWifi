package com.practice.securewifi.data.password_lists.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practice.securewifi.data.password_lists.entity.PersonInfo

@Dao
interface PersonInfoDao {

    @Query("SELECT * FROM person_info WHERE list_name = :passwordListName")
    suspend fun getPersonInfoForList(passwordListName: String): List<PersonInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersonInfo(personInfo: List<PersonInfo>)

    @Query("DELETE FROM person_info WHERE list_name = :listName")
    suspend fun deleteAllPersonInfoForList(listName: String)
}