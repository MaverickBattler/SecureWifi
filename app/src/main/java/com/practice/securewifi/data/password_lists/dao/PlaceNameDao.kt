package com.practice.securewifi.data.password_lists.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.practice.securewifi.data.password_lists.entity.PlaceName

@Dao
interface PlaceNameDao {

    @Query("SELECT * FROM place_name WHERE list_name = :passwordListName")
    suspend fun getPlaceNamesForList(passwordListName: String): List<PlaceName>

    @Insert
    suspend fun insertPlacesNames(placesNames: List<PlaceName>)

    @Query("DELETE FROM place_name WHERE id = :id")
    suspend fun removePlaceName(id: Int)

    @Query("DELETE FROM place_name WHERE list_name = :listName")
    suspend fun deleteAllPlaceNamesForList(listName: String)
}