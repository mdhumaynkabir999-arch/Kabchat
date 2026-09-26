package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey val id: String,
    val name: String,
    val avatarColor: Long, // Color ARGB long
    val avatarEmoji: String,
    val statusMessage: String,
    val isOnline: Boolean,
    val lastSeen: String,
    val isGroup: Boolean = false,
    val membersCount: Int = 1,
    val phoneOrHandle: String = "",
    val unreadCount: Int = 0,
    val lastMessageText: String = "",
    val lastMessageTime: Long = 0L,
    val isPinned: Boolean = false
)
