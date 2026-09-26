package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ContactEntity
import com.example.data.model.StoryItem
import com.example.ui.ChatFilter
import com.example.ui.components.StoryCarousel
import com.example.ui.components.UserAvatar
import com.example.ui.theme.EmeraldOnline
import com.example.ui.theme.TealPrimary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatListTab(
    contacts: List<ContactEntity>,
    stories: List<StoryItem>,
    typingStatus: Map<String, Boolean>,
    activeFilter: ChatFilter,
    onSelectFilter: (ChatFilter) -> Unit,
    onSelectContact: (String) -> Unit,
    onSelectStory: (StoryItem) -> Unit,
    onAddStory: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        // Active Stories Carousel
        StoryCarousel(
            stories = stories,
            onSelectStory = onSelectStory,
            onAddStory = onAddStory
        )

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
            thickness = 0.8.dp
        )

        // Filter chips row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ChatFilter.values().forEach { filter ->
                val selected = activeFilter == filter
                FilterChip(
                    selected = selected,
                    onClick = { onSelectFilter(filter) },
                    label = {
                        Text(
                            text = filter.titleBn,
                            fontSize = 12.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = TealPrimary.copy(alpha = 0.15f),
                        selectedLabelColor = TealPrimary
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }

        // Chat list or empty state
        if (contacts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "💬", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "কোনো বার্তা পাওয়া যায়নি",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "নতুন আলাপ শুরু করতে নিচের বোতামে চাপ দিন",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(contacts, key = { it.id }) { contact ->
                    val isTyping = typingStatus[contact.id] == true
                    ChatItemRow(
                        contact = contact,
                        isTyping = isTyping,
                        onClick = { onSelectContact(contact.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ChatItemRow(
    contact: ContactEntity,
    isTyping: Boolean,
    onClick: () -> Unit
) {
    val timeFormatted = if (contact.lastMessageTime > 0) {
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        sdf.format(Date(contact.lastMessageTime))
    } else ""

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        UserAvatar(
            name = contact.name,
            emoji = contact.avatarEmoji,
            avatarColor = contact.avatarColor,
            size = 52.dp,
            isOnline = contact.isOnline,
            showOnlineBadge = !contact.isGroup
        )

        Spacer(modifier = Modifier.width(14.dp))

        // Center: Name and last message
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Text(
                        text = contact.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (contact.unreadCount > 0) FontWeight.Bold else FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (contact.isPinned) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.PushPin,
                            contentDescription = "Pinned",
                            tint = TealPrimary,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }

                Text(
                    text = timeFormatted,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (contact.unreadCount > 0) TealPrimary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    fontWeight = if (contact.unreadCount > 0) FontWeight.Bold else FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isTyping) {
                    Text(
                        text = "✍️ টাইপ করছে…",
                        color = EmeraldOnline,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    Text(
                        text = contact.lastMessageText.ifBlank { contact.statusMessage },
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (contact.unreadCount > 0) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = if (contact.unreadCount > 0) FontWeight.Medium else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }

                if (contact.unreadCount > 0) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(TealPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = contact.unreadCount.toString(),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
