package com.sample.androidfundamental2.data.repository

import androidx.lifecycle.LiveData
import com.sample.androidfundamental2.data.local.dao.FavoriteEventDao
import com.sample.androidfundamental2.data.local.entity.FavoriteEvent
import com.sample.androidfundamental2.data.model.Event
import com.sample.androidfundamental2.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventRepository(
    private val apiService: ApiService,
    private val favoriteEventDao: FavoriteEventDao
) {
    
    suspend fun getUpcomingEvents(limit: Int? = null): Result<List<Event>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getEvents(active = 1, limit = limit)
                if (!response.error) {
                    Result.success(response.listEvents)
                } else {
                    Result.failure(Exception(response.message))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun getFinishedEvents(limit: Int? = null): Result<List<Event>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getEvents(active = 0, limit = limit)
                if (!response.error) {
                    Result.success(response.listEvents)
                } else {
                    Result.failure(Exception(response.message))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun searchEvents(keyword: String): Result<List<Event>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.searchEvents(keyword = keyword)
                if (!response.error) {
                    Result.success(response.listEvents)
                } else {
                    Result.failure(Exception(response.message))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun getEventDetail(id: Int): Result<Event> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getEventDetail(id)
                if (!response.error) {
                    Result.success(response.event)
                } else {
                    Result.failure(Exception(response.message))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    // Favorite operations
    fun getAllFavoriteEvents(): LiveData<List<FavoriteEvent>> {
        return favoriteEventDao.getAllFavoriteEvents()
    }

    suspend fun getFavoriteEventById(eventId: Int): FavoriteEvent? {
        return withContext(Dispatchers.IO) {
            favoriteEventDao.getFavoriteEventById(eventId)
        }
    }

    fun isEventFavorite(eventId: Int): LiveData<Boolean> {
        return favoriteEventDao.isEventFavorite(eventId)
    }

    suspend fun insertFavoriteEvent(event: Event) {
        withContext(Dispatchers.IO) {
            val favoriteEvent = FavoriteEvent(
                id = event.id,
                name = event.name,
                summary = event.summary,
                description = event.description,
                imageLogo = event.imageLogo,
                mediaCover = event.mediaCover,
                category = event.category,
                ownerName = event.ownerName,
                cityName = event.cityName,
                quota = event.quota,
                registrants = event.registrants,
                beginTime = event.beginTime,
                endTime = event.endTime,
                link = event.link
            )
            favoriteEventDao.insertFavoriteEvent(favoriteEvent)
        }
    }

    suspend fun restoreFavoriteEvent(favoriteEvent: FavoriteEvent) {
        withContext(Dispatchers.IO) {
            favoriteEventDao.insertFavoriteEvent(favoriteEvent)
        }
    }

    suspend fun deleteFavoriteEventById(eventId: Int) {
        withContext(Dispatchers.IO) {
            favoriteEventDao.deleteFavoriteEventById(eventId)
        }
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null

        fun getInstance(apiService: ApiService, favoriteEventDao: FavoriteEventDao): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, favoriteEventDao).also { instance = it }
            }
    }
}
