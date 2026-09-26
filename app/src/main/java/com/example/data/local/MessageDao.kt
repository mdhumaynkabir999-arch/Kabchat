package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp ASC")
    fun getMessagesForChat(chatId: String): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp DESC LIMIT 1")
    fun getLastMessageForChat(chatId: String): Flow<MessageEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Update
    suspend fun updateMessage(message: MessageEntity)

    @Query("UPDATE messages SET reaction = :reaction WHERE id = :messageId")
    suspend fun updateReaction(messageId: Long, reaction: String?)

    @Query("UPDATE messages SET isStarred = NOT isStarred WHERE id = :messageId")
    suspend fun toggleStar(messageId: Long)

    @Query("DELETE FROM messages WHERE id = :messageId")
    suspend fun deleteMessage(messageId: Long)

    @Query("DELETE FROM messages WHERE chatId = :chatId")
    suspend fun clearChat(chatId: String)

    @Query("SELECT * FROM messages WHERE text LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchMessages(query: String): Flow<List<MessageEntity>>

    @Query("SELECT COUNT(*) FROM messages WHERE chatId = :chatId")
    suspend fun getMessageCount(chatId: String): Int
}
