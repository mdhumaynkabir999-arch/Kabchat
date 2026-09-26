package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: String = "me",
    val displayName: String = "হুমায়ুন কবির",
    val username: String = "@humayun_kabir",
    val bio: String = "আলাপ অ্যাপে সক্রিয় 🌟 | সবসময় মেসেজে কানেক্টেড",
    val phone: String = "+880 1712-345678",
    val avatarEmoji: String = "👨‍💻",
    val avatarColor: Long = 0xFF0F766EL,
    val isOnline: Boolean = true,
    val isBangla: Boolean = true,
    val wallpaperTheme: String = "emerald" // "emerald", "dark", "classic", "rose"
)

data class StoryItem(
    val id: String,
    val userName: String,
    val userEmoji: String,
    val avatarColor: Long,
    val isSeen: Boolean = false,
    val timeAgo: String,
    val caption: String = ""
)
