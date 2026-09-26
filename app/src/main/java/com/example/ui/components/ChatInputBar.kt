package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MessageType
import com.example.ui.theme.TealPrimary

@Composable
fun ChatInputBar(
    onSendMessage: (String, MessageType, String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }
    var showAttachMenu by remember { mutableStateOf(false) }
    var showEmojiQuickRow by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .navigationBarsPadding()
            .imePadding()
    ) {
        // Quick emoji picker drawer
        AnimatedVisibility(visible = showEmojiQuickRow) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("😊", "❤️", "😂", "🔥", "👍", "🙌", "🎉", "☕", "🇧🇩", "✨").forEach { emoji ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(CircleShape)
                            .clickable { text += emoji }
                            .padding(vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = emoji, fontSize = 20.sp)
                    }
                }
            }
        }

        // Smart reply suggestions
        SmartReplyBar(
            onSelectSuggestion = { suggestion ->
                onSendMessage(suggestion, MessageType.TEXT, null)
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Attachment action
            Box {
                IconButton(
                    onClick = { showAttachMenu = true },
                    modifier = Modifier.size(42.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Attach",
                        tint = TealPrimary
                    )
                }

                DropdownMenu(
                    expanded = showAttachMenu,
                    onDismissRequest = { showAttachMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("ছবি / ফটো গ্যালারি 🖼️") },
                        leadingIcon = { Icon(Icons.Default.Image, contentDescription = null, tint = TealPrimary) },
                        onClick = {
                            showAttachMenu = false
                            onSendMessage("একটি সুন্দর ছবি শেয়ার করেছেন 📷", MessageType.IMAGE, "image_preview")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("ভয়েস নোট 🎙️") },
                        leadingIcon = { Icon(Icons.Default.Mic, contentDescription = null, tint = TealPrimary) },
                        onClick = {
                            showAttachMenu = false
                            onSendMessage("ভয়েস মেসেজ (০:১৬)", MessageType.VOICE, "0:16")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("লাইভ লোকেশন 📍") },
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = TealPrimary) },
                        onClick = {
                            showAttachMenu = false
                            onSendMessage("ধানমন্ডি ২৭, ঢাকা, বাংলাদেশ", MessageType.LOCATION, "ধানমন্ডি লেক, ঢাকা")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("ডকুমেন্ট ও ফাইল 📁") },
                        leadingIcon = { Icon(Icons.Default.AttachFile, contentDescription = null, tint = TealPrimary) },
                        onClick = {
                            showAttachMenu = false
                            onSendMessage("প্রজেক্ট_নোটস.pdf (2.4 MB)", MessageType.DOCUMENT, "document_file")
                        }
                    )
                }
            }

            // Emoji toggle
            IconButton(
                onClick = { showEmojiQuickRow = !showEmojiQuickRow },
                modifier = Modifier.size(38.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SentimentSatisfiedAlt,
                    contentDescription = "Emojis",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Input TextField
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = {
                    Text(
                        text = "বার্তা লিখুন…",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TealPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ),
                maxLines = 4
            )

            Spacer(modifier = Modifier.width(4.dp))

            // Send / Mic Button
            val isTyping = text.isNotBlank()
            Surface(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .clickable {
                        if (isTyping) {
                            onSendMessage(text.trim(), MessageType.TEXT, null)
                            text = ""
                        } else {
                            // Quick voice note send simulation
                            onSendMessage("ভয়েস নোট (০:১২)", MessageType.VOICE, "0:12")
                        }
                    },
                shape = CircleShape,
                color = TealPrimary,
                shadowElevation = 2.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (isTyping) Icons.AutoMirrored.Filled.Send else Icons.Default.Mic,
                        contentDescription = if (isTyping) "Send" else "Voice record",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
