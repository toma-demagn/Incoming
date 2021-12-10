package com.example.tutorapp.data.model

/**
 * Data class for a Message object.
 */
data class Message(
    val id: Int ?= null,
    val socketId: Int,
    val isFromSender: Boolean,
    val content: String,
    val read: Boolean = true,
    val time: String
)
