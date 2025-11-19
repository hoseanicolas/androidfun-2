package com.sample.androidfundamental2.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_events")
data class FavoriteEvent(
    @PrimaryKey
    val id: Int,
    val name: String,
    val summary: String?,
    val description: String,
    val imageLogo: String?,
    val mediaCover: String?,
    val category: String?,
    val ownerName: String,
    val cityName: String?,
    val quota: Int,
    val registrants: Int,
    val beginTime: String,
    val endTime: String?,
    val link: String
) {
    val remainingQuota: Int
        get() = quota - registrants

    val imageUrl: String
        get() = mediaCover ?: imageLogo ?: ""
}
