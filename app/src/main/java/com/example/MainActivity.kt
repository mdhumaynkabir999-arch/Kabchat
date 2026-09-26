package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.data.local.AppDatabase
import com.example.data.repository.ChatRepository
import com.example.ui.ChatViewModel
import com.example.ui.screens.MainScreen
import com.example.ui.theme.AalapTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = AppDatabase.getInstance(applicationContext)
        val repository = ChatRepository(
            contactDao = db.contactDao(),
            messageDao = db.messageDao(),
            scope = lifecycleScope
        )

        setContent {
            val viewModel = remember {
                ChatViewModel(repository)
            }

            AalapTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen(viewModel = viewModel)
                }
            }
        }
    }
}
