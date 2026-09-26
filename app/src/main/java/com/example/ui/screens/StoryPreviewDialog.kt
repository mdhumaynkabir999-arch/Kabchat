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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.StoryItem
import com.example.ui.components.UserAvatar

@Composable
fun StoryPreviewDialog(
    story: StoryItem,
    onDismiss: () -> Unit,
    onReplyToStory: (String) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(story.avatarColor).copy(alpha = 0.85f),
                                Color(0xFF0F172A),
                                Color.Black
                            )
                        )
                    )
                    .clickable { onDismiss() }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Header with progress bar, avatar, name, close
                    Column(modifier = Modifier.padding(top = 28.dp)) {
                        // Progress bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color.White.copy(alpha = 0.4f))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .height(3.dp)
                                    .background(Color.White)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                UserAvatar(
                                    name = story.userName,
                                    emoji = story.userEmoji,
                                    avatarColor = story.avatarColor,
                                    size = 38.dp,
                                    showOnlineBadge = false
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = story.userName,
                                        color = Color.White,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = story.timeAgo,
                                        color = Color.White.copy(alpha = 0.7f),
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            IconButton(onClick = onDismiss) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    // Content Center
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = story.userEmoji,
                            fontSize = 72.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = story.caption,
                            color = Color.White,
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }

                    // Bottom Quick Reaction
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf("❤️", "🔥", "😂", "👏", "😍").forEach { emoji ->
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f))
                                    .clickable {
                                        onReplyToStory("রিপ্লাই পাঠিয়েছেন: $emoji")
                                        onDismiss()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = emoji, fontSize = 22.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
