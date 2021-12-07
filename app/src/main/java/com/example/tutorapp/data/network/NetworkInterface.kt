package com.example.tutorapp.data.network

import com.example.tutorapp.data.model.Login
import com.example.tutorapp.data.model.Tag
import com.example.tutorapp.data.model.User
import retrofit2.http.*

interface NetworkInterface {

    companion object {
        var BaseURl = "http://192.168.0.165:3000"
    }

    @GET("/api/tags")
    suspend fun getTags(): List<Tag>

    @GET("/api/logins/{id}")
    suspend fun getLogin(@Path("id") id: String): Login

    @Headers("Content-Type: application/json")
    @POST("/api/logins")
    suspend fun createLogin(@Body login: Login): Login

    @DELETE("/api/logins/{id}")
    suspend fun deleteLogin(@Path("id") id: String)

    @GET("/api/users")
    suspend fun getUserByEmail(@Query("email") email: String): User

    @Headers("Content-Type: application/json")
    @POST("/api/users")
    suspend fun createUser(@Body user: User): User
}