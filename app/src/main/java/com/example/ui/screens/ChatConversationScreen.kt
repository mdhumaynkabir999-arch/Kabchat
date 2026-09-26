package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ContactEntity
import com.example.data.model.MessageEntity
import com.example.data.model.MessageType
import com.example.ui.components.ChatInputBar
import com.example.ui.components.MessageBubble
import com.example.ui.components.UserAvatar
import com.example.ui.theme.EmeraldOnline
import com.example.ui.theme.TealPrimary

@Composable
fun ChatConversationScreen(
    contact: ContactEntity,
    messages: List<MessageEntity>,
    isTyping: Boolean,
    isDarkTheme: Boolean,
    onBack: () -> Unit,
    onSendMessage: (String, MessageType, String?) -> Unit,
    onReactToMessage: (Long, String) -> Unit,
    onToggleStar: (Long) -> Unit,
    onDeleteMessage: (Long) -> Unit,
    onClearChat: () -> Unit,
    onStartCall: (isVideo: Boolean) -> Unit
) {
    BackHandler {
        onBack()
    }

    val listState = rememberLazyListState()
    var showMenu by remember { mutableStateOf(false) }

    LaunchedEffect(messages.size, isTyping) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 4.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TealPrimary
                        )
                    }

                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { /* Could show info */ },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        UserAvatar(
                            name = contact.name,
                            emoji = contact.avatarEmoji,
                            avatarColor = contact.avatarColor,
                            size = 40.dp,
                            isOnline = contact.isOnline,
                            showOnlineBadge = !contact.isGroup
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = contact.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            if (isTyping) {
                                Text(
                                    text = "✍️ টাইপ করছে…",
                                    color = EmeraldOnline,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            } else {
                                Text(
                                    text = if (contact.isOnline) "🟢 সক্রিয় (অনলাইন)" else contact.lastSeen,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (contact.isOnline) EmeraldOnline else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // Call buttons
                    IconButton(onClick = { onStartCall(false) }) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Voice Call",
                            tint = TealPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    IconButton(onClick = { onStartCall(true) }) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = "Video Call",
                            tint = TealPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Menu",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("সব চ্যাট মুছুন") },
                                leadingIcon = { Icon(Icons.Default.DeleteSweep, contentDescription = null, tint = Color.Red) },
                                onClick = {
                                    showMenu = false
                                    onClearChat()
                                }
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            ChatInputBar(
                onSendMessage = onSendMessage
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Subtle wallpaper background image
            Image(
                painter = painterResource(id = R.drawable.bg_chat_wallpaper),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = if (isDarkTheme) 0.05f else 0.12f
            )

            // Chat Messages List
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(vertical = 8.dp)
                ) {
                    items(messages, key = { it.id }) { msg ->
                        MessageBubble(
                            message = msg,
                            isGroupChat = contact.isGroup,
                            isDark = isDarkTheme,
                            onReact = { emoji -> onReactToMessage(msg.id, emoji) },
                            onToggleStar = { onToggleStar(msg.id) },
                            onDelete = { onDeleteMessage(msg.id) }
                        )
                    }

                    // Live typing indicator bubble
                    if (isTyping) {
                        item(key = "typing_indicator") {
                            Box(
                                modifier = Modifier
                                    .padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${contact.name} টাইপ করছে…",
                                        fontSize = 12.sp,
                                        color = EmeraldOnline,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(EmeraldOnline)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
