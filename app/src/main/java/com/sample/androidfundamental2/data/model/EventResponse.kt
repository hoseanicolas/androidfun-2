package com.sample.androidfundamental2.data.model

import com.google.gson.annotations.SerializedName

data class EventResponse(
    @SerializedName("error")
    val error: Boolean,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("listEvents")
    val listEvents: List<Event>
)

data class EventDetailResponse(
    @SerializedName("error")
    val error: Boolean,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("event")
    val event: Event
)
