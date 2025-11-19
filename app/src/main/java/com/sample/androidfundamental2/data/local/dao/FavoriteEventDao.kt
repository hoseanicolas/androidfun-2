package com.sample.androidfundamental2.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sample.androidfundamental2.data.local.entity.FavoriteEvent

@Dao
interface FavoriteEventDao {

    @Query("SELECT * FROM favorite_events ORDER BY beginTime ASC")
    fun getAllFavoriteEvents(): LiveData<List<FavoriteEvent>>

    @Query("SELECT * FROM favorite_events WHERE id = :eventId")
    suspend fun getFavoriteEventById(eventId: Int): FavoriteEvent?

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_events WHERE id = :eventId)")
    fun isEventFavorite(eventId: Int): LiveData<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteEvent(event: FavoriteEvent)

    @Delete
    suspend fun deleteFavoriteEvent(event: FavoriteEvent)

    @Query("DELETE FROM favorite_events WHERE id = :eventId")
    suspend fun deleteFavoriteEventById(eventId: Int)
}
