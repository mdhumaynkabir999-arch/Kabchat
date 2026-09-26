package com.example.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.MessageEntity
import com.example.data.model.MessageStatus
import com.example.data.model.MessageType
import com.example.ui.theme.BubbleReceivedDark
import com.example.ui.theme.BubbleReceivedLight
import com.example.ui.theme.BubbleSentDark
import com.example.ui.theme.BubbleSentLight
import com.example.ui.theme.TealPrimary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageBubble(
    message: MessageEntity,
    isGroupChat: Boolean = false,
    isDark: Boolean = false,
    onReact: (String) -> Unit = {},
    onDelete: () -> Unit = {},
    onToggleStar: () -> Unit = {}
) {
    val isMe = message.senderId == "me"
    var showActionDialog by remember { mutableStateOf(false) }

    val bubbleColor = if (isMe) {
        if (isDark) BubbleSentDark else BubbleSentLight
    } else {
        if (isDark) BubbleReceivedDark else BubbleReceivedLight
    }

    val shape = if (isMe) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    } else {
        RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    }

    val timeString = remember(message.timestamp) {
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        sdf.format(Date(message.timestamp))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 3.dp),
        contentAlignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier.widthIn(max = 310.dp),
            horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
        ) {
            Surface(
                modifier = Modifier
                    .clip(shape)
                    .combinedClickable(
                        onClick = {},
                        onLongClick = { showActionDialog = true }
                    ),
                shape = shape,
                color = bubbleColor,
                shadowElevation = 1.dp
            ) {
                Column(
                    modifier = Modifier.padding(
                        start = 12.dp,
                        end = 12.dp,
                        top = 8.dp,
                        bottom = 6.dp
                    )
                ) {
                    // Group sender name if received in group
                    if (!isMe && isGroupChat) {
                        Text(
                            text = message.senderName,
                            style = MaterialTheme.typography.labelMedium,
                            color = TealPrimary,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(bottom = 3.dp)
                        )
                    }

                    // Content according to type
                    when (message.type) {
                        MessageType.VOICE.name -> {
                            VoiceWaveVisualizer(
                                duration = message.mediaUrlOrExtra ?: "0:14",
                                isSender = isMe,
                                modifier = Modifier.width(220.dp)
                            )
                        }
                        MessageType.IMAGE.name -> {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(160.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            Brush.linearGradient(
                                                listOf(
                                                    Color(0xFF0F766E).copy(alpha = 0.8f),
                                                    Color(0xFF14B8A6).copy(alpha = 0.9f)
                                                )
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            imageVector = Icons.Default.Image,
                                            contentDescription = "Image preview",
                                            tint = Color.White,
                                            modifier = Modifier.size(36.dp)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "ফটোগ্রাফি শেয়ার করা হয়েছে 📷",
                                            color = Color.White,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                                if (message.text.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = message.text,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                        MessageType.LOCATION.name -> {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(TealPrimary.copy(alpha = 0.1f))
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Location",
                                    tint = Color.Red,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = "বর্তমান অবস্থান",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = message.mediaUrlOrExtra ?: message.text,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                        else -> {
                            Text(
                                text = message.text,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 15.sp,
                                lineHeight = 20.sp
                            )
                        }
                    }

                    // Bottom info: time, star, status ticks
                    Row(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        if (message.isStarred) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Starred",
                                tint = Color(0xFFF59E0B),
                                modifier = Modifier
                                    .size(12.dp)
                                    .padding(end = 3.dp)
                            )
                        }

                        Text(
                            text = timeString,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )

                        if (isMe) {
                            Spacer(modifier = Modifier.width(4.dp))
                            when (message.status) {
                                MessageStatus.READ.name -> {
                                    Icon(
                                        imageVector = Icons.Default.DoneAll,
                                        contentDescription = "Read",
                                        tint = Color(0xFF0284C7),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                MessageStatus.DELIVERED.name -> {
                                    Icon(
                                        imageVector = Icons.Default.DoneAll,
                                        contentDescription = "Delivered",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                else -> {
                                    Icon(
                                        imageVector = Icons.Default.Done,
                                        contentDescription = "Sent",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Reaction Pill badge if present
            if (!message.reaction.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .padding(top = 2.dp, start = 4.dp, end = 4.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = message.reaction,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }

    // Reaction & Action Dialog
    if (showActionDialog) {
        Dialog(onDismissRequest = { showActionDialog = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "প্রতিক্রিয়া দিন",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("❤️", "👍", "😂", "😮", "😢", "🔥").forEach { emoji ->
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .combinedClickable(
                                        onClick = {
                                            onReact(emoji)
                                            showActionDialog = false
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = emoji, fontSize = 20.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.combinedClickable(
                                onClick = {
                                    onToggleStar()
                                    showActionDialog = false
                                }
                            )
                        ) {
                            Text(
                                text = if (message.isStarred) "আনস্টার ⭐" else "স্টার মার্ক ⭐",
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                fontSize = 13.sp
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFFEE2E2),
                            modifier = Modifier.combinedClickable(
                                onClick = {
                                    onDelete()
                                    showActionDialog = false
                                }
                            )
                        ) {
                            Text(
                                text = "মুছে ফেলুন 🗑️",
                                color = Color.Red,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
