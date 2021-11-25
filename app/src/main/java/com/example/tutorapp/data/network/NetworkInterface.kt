package com.example.tutorapp.data.network

import com.example.tutorapp.data.model.Login
import com.example.tutorapp.data.model.Tag
import com.example.tutorapp.data.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface NetworkInterface {

    companion object {
        var BaseURl = "https://apitutorapp.herokuapp.com"
    }

    @GET("/api/tags")
    suspend fun getTags(): List<Tag>

    @Headers("Content-Type: application/json")
    @POST("/api/logins")
    suspend fun createLogin(@Body login: Login): Login

    @Headers("Content-Type: application/json")
    @POST("/api/users")
    suspend fun createUser(@Body user: User): User
}