package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StoryItem
import com.example.ui.theme.EmeraldOnline
import com.example.ui.theme.TealPrimary

@Composable
fun StoryCarousel(
    stories: List<StoryItem>,
    onSelectStory: (StoryItem) -> Unit,
    onAddStory: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // My Story circle
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(62.dp)
                .clickable { onAddStory() }
        ) {
            Box(
                modifier = Modifier.size(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(TealPrimary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "👨‍💻", fontSize = 24.sp)
                }

                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(TealPrimary)
                        .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add status",
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "আমার স্ট্যাটাস",
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Friends stories
        stories.forEach { story ->
            val ringBrush = if (story.isSeen) {
                Brush.linearGradient(listOf(Color.LightGray, Color.Gray))
            } else {
                Brush.sweepGradient(listOf(TealPrimary, EmeraldOnline, Color(0xFF38BDF8), TealPrimary))
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(62.dp)
                    .clickable { onSelectStory(story) }
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .border(2.5.dp, ringBrush, CircleShape)
                        .padding(3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color(story.avatarColor)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = story.userEmoji, fontSize = 22.sp)
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = story.userName.split(" ").firstOrNull() ?: story.userName,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
