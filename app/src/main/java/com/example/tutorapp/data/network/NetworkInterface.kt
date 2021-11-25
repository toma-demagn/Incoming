package com.example.tutorapp.data.network

import com.example.tutorapp.data.model.Login
import com.example.tutorapp.data.model.Tag
import com.example.tutorapp.data.model.User
import retrofit2.http.*

interface NetworkInterface {

    companion object {
        var BaseURl = "https://apitutorapp.herokuapp.com"
    }

    @GET("/api/tags")
    suspend fun getTags(): List<Tag>

    @Headers("Content-Type: application/json")
    @POST("/api/logins")
    suspend fun createLogin(@Body login: Login): Login

    @DELETE("/api/logins/{id}")
    suspend fun deleteLogin(@Path("id") id : String)

    @Headers("Content-Type: application/json")
    @POST("/api/users")
    suspend fun createUser(@Body user: User): User
}