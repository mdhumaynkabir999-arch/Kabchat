package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.ChatViewModel
import com.example.ui.MainTab
import com.example.ui.theme.EmeraldOnline
import com.example.ui.theme.TealPrimary

@Composable
fun MainScreen(
    viewModel: ChatViewModel,
    modifier: Modifier = Modifier
) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val activeFilter by viewModel.chatFilter.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val contacts by viewModel.filteredChatList.collectAsStateWithLifecycle()
    val onlineContacts by viewModel.onlineContacts.collectAsStateWithLifecycle()
    val groups by viewModel.groups.collectAsStateWithLifecycle()
    val stories by viewModel.stories.collectAsStateWithLifecycle()
    val typingStatus by viewModel.typingStatus.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    val selectedChatId by viewModel.selectedChatId.collectAsStateWithLifecycle()
    val selectedContact by viewModel.selectedChatContact.collectAsStateWithLifecycle()
    val chatMessages by viewModel.currentChatMessages.collectAsStateWithLifecycle()

    val activeCall by viewModel.activeCall.collectAsStateWithLifecycle()
    val showNewChatDialog by viewModel.showNewChatDialog.collectAsStateWithLifecycle()
    val showStoryPreview by viewModel.showStoryPreview.collectAsStateWithLifecycle()

    val isDark = isSystemInDarkTheme()
    var isSearchExpanded by remember { mutableStateOf(false) }

    // If a chat conversation is opened, show ChatConversationScreen
    if (selectedChatId != null && selectedContact != null) {
        ChatConversationScreen(
            contact = selectedContact!!,
            messages = chatMessages,
            isTyping = typingStatus[selectedContact!!.id] == true,
            isDarkTheme = isDark,
            onBack = { viewModel.closeChat() },
            onSendMessage = { text, type, extra -> viewModel.sendMessage(text, type, extra) },
            onReactToMessage = { id, emoji -> viewModel.reactToMessage(id, emoji) },
            onToggleStar = { id -> viewModel.toggleStarMessage(id) },
            onDeleteMessage = { id -> viewModel.deleteMessage(id) },
            onClearChat = { viewModel.clearChat(selectedContact!!.id) },
            onStartCall = { isVideo -> viewModel.startCall(selectedContact!!, isVideo) }
        )
    } else {
        Scaffold(
            topBar = {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Logo and Title
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(TealPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "আ",
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "আলাপ",
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = TealPrimary
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(EmeraldOnline)
                                        )
                                    }
                                    Text(
                                        text = "অনলাইন চ্যাট ও যোগাযোগ",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            // Search toggle icon
                            IconButton(
                                onClick = {
                                    isSearchExpanded = !isSearchExpanded
                                    if (!isSearchExpanded) viewModel.setSearchQuery("")
                                },
                                modifier = Modifier.testTag("search_toggle_button")
                            ) {
                                Icon(
                                    imageVector = if (isSearchExpanded) Icons.Default.Close else Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = TealPrimary
                                )
                            }

                            // New chat action
                            IconButton(
                                onClick = { viewModel.openNewChatDialog() },
                                modifier = Modifier.testTag("add_chat_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "New Chat",
                                    tint = TealPrimary
                                )
                            }
                        }

                        // Search Input Bar if expanded
                        AnimatedVisibility(visible = isSearchExpanded) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { viewModel.setSearchQuery(it) },
                                placeholder = { Text("আলাপ বা বার্তা খুঁজুন…", fontSize = 14.sp) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                trailingIcon = {
                                    if (searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                            Icon(Icons.Default.Close, contentDescription = "Clear")
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp
                ) {
                    // 1. Chats tab
                    NavigationBarItem(
                        selected = currentTab == MainTab.CHATS,
                        onClick = { viewModel.selectTab(MainTab.CHATS) },
                        icon = {
                            val totalUnread = contacts.sumOf { it.unreadCount }
                            BadgedBox(
                                badge = {
                                    if (totalUnread > 0) {
                                        Badge(containerColor = TealPrimary) {
                                            Text(text = totalUnread.toString())
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (currentTab == MainTab.CHATS) Icons.Default.Forum else Icons.Outlined.Forum,
                                    contentDescription = "Chats"
                                )
                            }
                        },
                        label = { Text(MainTab.CHATS.titleBn) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TealPrimary,
                            selectedTextColor = TealPrimary,
                            indicatorColor = TealPrimary.copy(alpha = 0.15f)
                        )
                    )

                    // 2. Online tab
                    NavigationBarItem(
                        selected = currentTab == MainTab.ONLINE,
                        onClick = { viewModel.selectTab(MainTab.ONLINE) },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (onlineContacts.isNotEmpty()) {
                                        Badge(containerColor = EmeraldOnline) {
                                            Text(onlineContacts.size.toString())
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (currentTab == MainTab.ONLINE) Icons.Default.Wifi else Icons.Outlined.Wifi,
                                    contentDescription = "Online"
                                )
                            }
                        },
                        label = { Text(MainTab.ONLINE.titleBn) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TealPrimary,
                            selectedTextColor = TealPrimary,
                            indicatorColor = TealPrimary.copy(alpha = 0.15f)
                        )
                    )

                    // 3. Groups tab
                    NavigationBarItem(
                        selected = currentTab == MainTab.GROUPS,
                        onClick = { viewModel.selectTab(MainTab.GROUPS) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == MainTab.GROUPS) Icons.Default.Group else Icons.Outlined.Group,
                                contentDescription = "Groups"
                            )
                        },
                        label = { Text(MainTab.GROUPS.titleBn) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TealPrimary,
                            selectedTextColor = TealPrimary,
                            indicatorColor = TealPrimary.copy(alpha = 0.15f)
                        )
                    )

                    // 4. Profile tab
                    NavigationBarItem(
                        selected = currentTab == MainTab.PROFILE,
                        onClick = { viewModel.selectTab(MainTab.PROFILE) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == MainTab.PROFILE) Icons.Default.Person else Icons.Outlined.Person,
                                contentDescription = "Profile"
                            )
                        },
                        label = { Text(MainTab.PROFILE.titleBn) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TealPrimary,
                            selectedTextColor = TealPrimary,
                            indicatorColor = TealPrimary.copy(alpha = 0.15f)
                        )
                    )
                }
            },
            floatingActionButton = {
                if (currentTab == MainTab.CHATS || currentTab == MainTab.GROUPS) {
                    FloatingActionButton(
                        onClick = { viewModel.openNewChatDialog() },
                        containerColor = TealPrimary,
                        contentColor = Color.White,
                        shape = CircleShape,
                        modifier = Modifier.testTag("fab_new_chat")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add New"
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentTab) {
                    MainTab.CHATS -> ChatListTab(
                        contacts = contacts,
                        stories = stories,
                        typingStatus = typingStatus,
                        activeFilter = activeFilter,
                        onSelectFilter = { viewModel.setFilter(it) },
                        onSelectContact = { id -> viewModel.openChat(id) },
                        onSelectStory = { story -> viewModel.openStory(story) },
                        onAddStory = { viewModel.openNewChatDialog() }
                    )
                    MainTab.ONLINE -> OnlineContactsTab(
                        onlineContacts = onlineContacts,
                        onSelectContact = { id -> viewModel.openChat(id) },
                        onStartCall = { contact -> viewModel.startCall(contact, false) }
                    )
                    MainTab.GROUPS -> GroupsTab(
                        groups = groups,
                        onSelectGroup = { id -> viewModel.openChat(id) },
                        onCreateGroup = { viewModel.openNewChatDialog() }
                    )
                    MainTab.PROFILE -> ProfileTab(
                        userProfile = userProfile,
                        onUpdateProfile = { name, bio, phone, isOnline, theme ->
                            viewModel.updateProfile(name, bio, phone, isOnline, theme)
                        }
                    )
                }
            }
        }
    }

    // New Chat / Group Dialog
    if (showNewChatDialog) {
        NewChatDialog(
            onDismiss = { viewModel.closeNewChatDialog() },
            onCreateContact = { name, handle, status, isGroup ->
                viewModel.createNewContactOrGroup(name, handle, status, isGroup)
            }
        )
    }

    // Call Simulation Dialog
    activeCall?.let { call ->
        CallSimulationDialog(
            callInfo = call,
            onToggleMute = { viewModel.toggleMuteCall() },
            onEndCall = { viewModel.endCall() }
        )
    }

    // Story Preview Dialog
    showStoryPreview?.let { story ->
        StoryPreviewDialog(
            story = story,
            onDismiss = { viewModel.closeStory() },
            onReplyToStory = { reply ->
                viewModel.openChat("tamim")
                viewModel.sendMessage(reply)
            }
        )
    }
}
