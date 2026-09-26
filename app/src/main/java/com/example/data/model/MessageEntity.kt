package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MessageType {
    TEXT,
    IMAGE,
    VOICE,
    LOCATION,
    DOCUMENT
}

enum class MessageStatus {
    SENDING,
    SENT,
    DELIVERED,
    READ
}

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val chatId: String,
    val senderId: String, // "me" or contact id
    val senderName: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = MessageStatus.READ.name,
    val type: String = MessageType.TEXT.name,
    val mediaUrlOrExtra: String? = null,
    val isStarred: Boolean = false,
    val reaction: String? = null,
    val replyToText: String? = null
)
