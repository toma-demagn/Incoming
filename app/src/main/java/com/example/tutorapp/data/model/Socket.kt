package com.example.tutorapp.data.model

/**
 * Data class for a Socket object.
 */
data class Socket(
    val id: Int? = null,
    val fromId: Int,
    val toId: Int,
    val creationTime: String,
    val lastUpdate: String,
    val lastMessage: String? = ""
)
