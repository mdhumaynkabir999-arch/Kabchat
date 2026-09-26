package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldOnline

@Composable
fun UserAvatar(
    name: String,
    emoji: String,
    avatarColor: Long,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    isOnline: Boolean = false,
    showOnlineBadge: Boolean = true
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        val baseColor = Color(avatarColor)
        val lighterColor = baseColor.copy(alpha = 0.8f)

        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(lighterColor, baseColor)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            if (emoji.isNotBlank()) {
                Text(
                    text = emoji,
                    fontSize = (size.value * 0.45).sp
                )
            } else {
                Text(
                    text = name.take(1).uppercase(),
                    color = Color.White,
                    fontSize = (size.value * 0.42).sp,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        if (isOnline && showOnlineBadge) {
            val badgeSize = (size.value * 0.28).dp.coerceAtLeast(10.dp)
            Box(
                modifier = Modifier
                    .size(badgeSize)
                    .align(Alignment.BottomEnd)
                    .offset(x = (-1).dp, y = (-1).dp)
                    .clip(CircleShape)
                    .background(EmeraldOnline)
                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
            )
        }
    }
}
