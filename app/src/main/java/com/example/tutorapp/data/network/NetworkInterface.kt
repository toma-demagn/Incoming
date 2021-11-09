package com.example.tutorapp.data.network

import com.example.tutorapp.data.model.Tag
import retrofit2.http.GET

interface NetworkInterface {

    @GET("/api/tags")
    suspend fun getTags(): List<Tag>
}