package com.sample.androidfundamental2.data.remote

import com.sample.androidfundamental2.data.model.EventDetailResponse
import com.sample.androidfundamental2.data.model.EventResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    
    @GET("events")
    suspend fun getEvents(
        @Query("active") active: Int,
        @Query("limit") limit: Int? = null
    ): EventResponse
    
    @GET("events")
    suspend fun searchEvents(
        @Query("active") active: Int = -1,
        @Query("q") keyword: String
    ): EventResponse
    
    @GET("events/{id}")
    suspend fun getEventDetail(
        @Path("id") id: Int
    ): EventDetailResponse
}
