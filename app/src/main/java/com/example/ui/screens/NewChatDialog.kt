package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.TealPrimary

@Composable
fun NewChatDialog(
    onDismiss: () -> Unit,
    onCreateContact: (name: String, handle: String, status: String, isGroup: Boolean) -> Unit
) {
    var isGroup by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var handle by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = if (isGroup) "নতুন গ্রুপ খুলুন 👥" else "নতুন আলাপ শুরু করুন 💬",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Selector: Direct vs Group
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = !isGroup,
                        onClick = { isGroup = false },
                        label = { Text("ব্যক্তিগত") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
                    )
                    FilterChip(
                        selected = isGroup,
                        onClick = { isGroup = true },
                        label = { Text("গ্রুপ চ্যাট") },
                        leadingIcon = { Icon(Icons.Default.Group, contentDescription = null) }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(if (isGroup) "গ্রুপের নাম" else "পরিচিতির নাম") },
                    placeholder = { Text(if (isGroup) "যেমন: সিলেট ট্রিপ ২০২৬" else "যেমন: আরিফুল ইসলাম") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = handle,
                    onValueChange = { handle = it },
                    label = { Text(if (isGroup) "বিবরণ বা ট্যাগ" else "মোবাইল নম্বর / ইউজারনেম") },
                    placeholder = { Text(if (isGroup) "যেমন: ট্যুর ও ঘোরাঘুরি" else "যেমন: 017xxxxxxxx") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = status,
                    onValueChange = { status = it },
                    label = { Text("স্ট্যাটাস বা বায়ো") },
                    placeholder = { Text("যেমন: আলাপেই আছি") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("বাতিল")
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                onCreateContact(name.trim(), handle.trim(), status.trim(), isGroup)
                            }
                        },
                        enabled = name.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("তৈরি করুন")
                    }
                }
            }
        }
    }
}
