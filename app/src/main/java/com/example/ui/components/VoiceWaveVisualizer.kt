package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TealPrimary

@Composable
fun VoiceWaveVisualizer(
    duration: String = "0:18",
    isSender: Boolean = false,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(false) }

    val transition = rememberInfiniteTransition(label = "wave")
    val animHeight1 by transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(350), RepeatMode.Reverse),
        label = "h1"
    )
    val animHeight2 by transition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(tween(420), RepeatMode.Reverse),
        label = "h2"
    )

    val barHeights = remember {
        listOf(0.35f, 0.7f, 0.45f, 0.9f, 0.6f, 0.8f, 0.5f, 0.75f, 0.4f, 0.65f, 0.85f, 0.3f, 0.7f, 0.55f, 0.9f)
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .padding(horizontal = 4.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Play / Pause Button
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (isSender) TealPrimary else MaterialTheme.colorScheme.primary)
                .clickable { isPlaying = !isPlaying },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Pause" else "Play",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Waveform Bars
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .height(24.dp),
                horizontalArrangement = Arrangement.spacedBy(2.5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                barHeights.forEachIndexed { index, defaultH ->
                    val factor = if (isPlaying) {
                        if (index % 2 == 0) animHeight1 else animHeight2
                    } else defaultH

                    val barHeight = (22 * factor).coerceIn(4f, 22f).dp
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .height(barHeight)
                            .clip(RoundedCornerShape(1.5.dp))
                            .background(
                                if (isSender) TealPrimary.copy(alpha = if (index < 7 || isPlaying) 0.9f else 0.4f)
                                else MaterialTheme.colorScheme.primary.copy(alpha = if (index < 7 || isPlaying) 0.9f else 0.4f)
                            )
                    )
                }
            }
            Text(
                text = if (isPlaying) "চলছে..." else duration,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
        }
    }
}
