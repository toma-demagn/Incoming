package com.example.tutorapp.data.network

import com.example.tutorapp.data.model.Socket
import com.example.tutorapp.data.model.Sockets
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SocketRetriever {

    private val networkInterface: NetworkInterface

    init {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(NetworkInterface.BaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        networkInterface = retrofit.create(NetworkInterface::class.java)
    }

    suspend fun getSocketsByUserId(userId: Int): Sockets {
        return networkInterface.getSocketsByUserId(userId)
    }

    suspend fun createSocket(socket: Socket): Socket {
        return networkInterface.createSocket(socket)
    }

}