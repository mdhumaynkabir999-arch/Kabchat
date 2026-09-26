package com.example.ui.screens

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.ActiveCallInfo
import com.example.ui.components.UserAvatar

@Composable
fun CallSimulationDialog(
    callInfo: ActiveCallInfo,
    onToggleMute: () -> Unit,
    onEndCall: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = "pulseAlpha"
    )

    Dialog(
        onDismissRequest = onEndCall,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF0F172A)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF042F2E), Color(0xFF0F172A), Color(0xFF020617))
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Top Info
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(top = 48.dp)
                    ) {
                        Text(
                            text = if (callInfo.isVideo) "ভিডিও কল 📹" else "ভয়েস কল 📞",
                            color = Color(0xFF2DD4BF),
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = callInfo.contact.name,
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (callInfo.isConnected) {
                                val mins = callInfo.durationSeconds / 60
                                val secs = callInfo.durationSeconds % 60
                                String.format("%02d:%02d", mins, secs)
                            } else "রিং হচ্ছে…",
                            color = Color(0xFF94A3B8),
                            fontSize = 15.sp,
                            modifier = Modifier.alpha(if (callInfo.isConnected) 1f else pulseAlpha)
                        )
                    }

                    // Middle Avatar
                    Box(
                        modifier = Modifier.size(160.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!callInfo.isConnected) {
                            Box(
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0F766E).copy(alpha = pulseAlpha * 0.3f))
                            )
                        }

                        UserAvatar(
                            name = callInfo.contact.name,
                            emoji = callInfo.contact.avatarEmoji,
                            avatarColor = callInfo.contact.avatarColor,
                            size = 110.dp,
                            showOnlineBadge = false
                        )
                    }

                    // Bottom Call Controls
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 36.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Mute button
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (callInfo.isMuted) Color.White.copy(alpha = 0.3f)
                                        else Color.White.copy(alpha = 0.15f)
                                    )
                                    .clickable { onToggleMute() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (callInfo.isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                                    contentDescription = "Mute",
                                    tint = Color.White
                                )
                            }

                            // End Call button
                            Box(
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEF4444))
                                    .clickable { onEndCall() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CallEnd,
                                    contentDescription = "End Call",
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            // Speaker or Video
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.15f))
                                    .clickable { },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (callInfo.isVideo) Icons.Default.Videocam else Icons.Default.VolumeUp,
                                    contentDescription = "Speaker",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
