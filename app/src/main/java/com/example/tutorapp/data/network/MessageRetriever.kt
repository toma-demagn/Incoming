package com.example.tutorapp.data.network

import com.example.tutorapp.data.model.Message
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Message retriever
 */
class MessageRetriever {

    private val networkInterface: NetworkInterface

    init {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(NetworkInterface.BaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        networkInterface = retrofit.create(NetworkInterface::class.java)
    }

    suspend fun getMessagesBySocketId(socketId: Int): List<Message> {
        return networkInterface.getMessagesBySocketId(socketId)
    }

    suspend fun createMessage(message: Message): Message {
        return networkInterface.createMessage(message)
    }
}