package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ContactEntity
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Query("SELECT * FROM contacts ORDER BY isPinned DESC, lastMessageTime DESC")
    fun getAllContacts(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contacts WHERE isGroup = 0 ORDER BY isOnline DESC, name ASC")
    fun getDirectContacts(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contacts WHERE isGroup = 1 ORDER BY lastMessageTime DESC")
    fun getGroups(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contacts WHERE isOnline = 1 ORDER BY name ASC")
    fun getOnlineContacts(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contacts WHERE id = :id LIMIT 1")
    fun getContactById(id: String): Flow<ContactEntity?>

    @Query("SELECT * FROM contacts WHERE id = :id LIMIT 1")
    suspend fun getContactDirect(id: String): ContactEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: ContactEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacts(contacts: List<ContactEntity>)

    @Update
    suspend fun updateContact(contact: ContactEntity)

    @Query("UPDATE contacts SET lastMessageText = :text, lastMessageTime = :time WHERE id = :id")
    suspend fun updateLastMessage(id: String, text: String, time: Long)

    @Query("UPDATE contacts SET unreadCount = 0 WHERE id = :id")
    suspend fun markChatAsRead(id: String)

    @Query("UPDATE contacts SET unreadCount = unreadCount + 1 WHERE id = :id")
    suspend fun incrementUnreadCount(id: String)

    @Query("UPDATE contacts SET isOnline = :isOnline, lastSeen = :lastSeen WHERE id = :id")
    suspend fun updateOnlineStatus(id: String, isOnline: Boolean, lastSeen: String)

    @Query("SELECT COUNT(*) FROM contacts")
    suspend fun getContactsCount(): Int

    // User Profile
    @Query("SELECT * FROM user_profile WHERE id = 'me' LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProfile(profile: UserProfile)
}
