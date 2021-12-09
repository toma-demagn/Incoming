package com.example.tutorapp.data.network

import com.example.tutorapp.data.model.*
import com.example.tutorapp.data.model.Tag
import retrofit2.http.*

interface NetworkInterface {

    companion object {
        var BaseURL = "http://192.168.0.165:3000"
        // var BaseURL = "https://apitutorapp.herokuapp.com"
    }

    /* LOGINS */

    @GET("/api/logins/{id}")
    suspend fun getLogin(@Path("id") id: String): Login

    @Headers("Content-Type: application/json")
    @POST("/api/logins")
    suspend fun createLogin(@Body login: Login): Login

    @Headers("Content-Type: application/json")
    @PUT("/api/logins/{id}")
    suspend fun updateLogin(@Path("id") id: String, @Body login: Login): Login

    @DELETE("/api/logins/{id}")
    suspend fun deleteLogin(@Path("id") id: String)

    /* MESSAGES */

    @GET("/api/messages")
    suspend fun getMessagesBySocketId(@Query("socketId") socketId: Int): List<Message>

    @Headers("Content-Type: application/json")
    @POST("/api/messages")
    suspend fun createMessage(@Body message: Message): Message

    /* SOCKETS */

    @GET("/api/sockets/{id}")
    suspend fun getSocketById(@Path("id") id: Int): Socket

    @GET("/api/sockets")
    suspend fun getSocketsByUserId(@Query("userId") userId: Int): List<Socket>

    @Headers("Content-Type: application/json")
    @POST("/api/sockets")
    suspend fun createSocket(@Body socket: Socket): Socket

    /* TAGS */

    @GET("/api/tags")
    suspend fun getTags(): List<Tag>

    /* USERS */

    @GET("/api/users/{id}")
    suspend fun getUserById(@Path("id") id: Int): User

    @GET("/api/users")
    suspend fun getUserByEmail(@Query("email") email: String): User

    @Headers("Content-Type: application/json")
    @POST("/api/users")
    suspend fun createUser(@Body user: User): User

    @Headers("Content-Type: application/json")
    @PUT("/api/users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: User): User
}