package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.ContactEntity
import com.example.data.model.MessageEntity
import com.example.data.model.MessageType
import com.example.data.model.StoryItem
import com.example.data.model.UserProfile
import com.example.data.repository.ChatRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MainTab(val titleBn: String, val titleEn: String) {
    CHATS("আলাপ", "Chats"),
    ONLINE("সক্রিয়", "Online"),
    GROUPS("গ্রুপ", "Groups"),
    PROFILE("প্রোফাইল", "Profile")
}

enum class ChatFilter(val titleBn: String) {
    ALL("সব"),
    DIRECT("ব্যক্তিগত"),
    GROUPS("গ্রুপ"),
    UNREAD("অপঠিত")
}

data class ActiveCallInfo(
    val contact: ContactEntity,
    val isVideo: Boolean,
    val isConnected: Boolean = false,
    val durationSeconds: Int = 0,
    val isMuted: Boolean = false
)

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {

    private val _currentTab = MutableStateFlow(MainTab.CHATS)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    private val _chatFilter = MutableStateFlow(ChatFilter.ALL)
    val chatFilter: StateFlow<ChatFilter> = _chatFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val allContacts: StateFlow<List<ContactEntity>> = repository.allContacts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val directContacts: StateFlow<List<ContactEntity>> = repository.directContacts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val groups: StateFlow<List<ContactEntity>> = repository.groups
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val onlineContacts: StateFlow<List<ContactEntity>> = repository.onlineContacts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userProfile: StateFlow<UserProfile> = repository.userProfile
        .combine(flowOf(null)) { profile, _ ->
            profile ?: UserProfile()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfile())

    val stories: StateFlow<List<StoryItem>> = repository.stories
    val typingStatus: StateFlow<Map<String, Boolean>> = repository.typingStatus

    // Filtered contact list based on search and selected filter chip
    val filteredChatList: StateFlow<List<ContactEntity>> = combine(
        allContacts,
        _searchQuery,
        _chatFilter
    ) { list, query, filter ->
        list.filter { contact ->
            val matchesFilter = when (filter) {
                ChatFilter.ALL -> true
                ChatFilter.DIRECT -> !contact.isGroup
                ChatFilter.GROUPS -> contact.isGroup
                ChatFilter.UNREAD -> contact.unreadCount > 0
            }
            val matchesQuery = query.isBlank() ||
                contact.name.contains(query, ignoreCase = true) ||
                contact.lastMessageText.contains(query, ignoreCase = true) ||
                contact.statusMessage.contains(query, ignoreCase = true)

            matchesFilter && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active opened chat
    private val _selectedChatId = MutableStateFlow<String?>(null)
    val selectedChatId: StateFlow<String?> = _selectedChatId.asStateFlow()

    val selectedChatContact: StateFlow<ContactEntity?> = _selectedChatId
        .flatMapLatest { id ->
            if (id == null) flowOf(null) else repository.getContactById(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val currentChatMessages: StateFlow<List<MessageEntity>> = _selectedChatId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList()) else repository.getMessagesForChat(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Call Simulation
    private val _activeCall = MutableStateFlow<ActiveCallInfo?>(null)
    val activeCall: StateFlow<ActiveCallInfo?> = _activeCall.asStateFlow()
    private var callTimerJob: Job? = null

    // Dialogs
    private val _showNewChatDialog = MutableStateFlow(false)
    val showNewChatDialog: StateFlow<Boolean> = _showNewChatDialog.asStateFlow()

    private val _showStoryPreview = MutableStateFlow<StoryItem?>(null)
    val showStoryPreview: StateFlow<StoryItem?> = _showStoryPreview.asStateFlow()

    fun selectTab(tab: MainTab) {
        _currentTab.value = tab
    }

    fun setFilter(filter: ChatFilter) {
        _chatFilter.value = filter
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun openChat(contactId: String) {
        _selectedChatId.value = contactId
        viewModelScope.launch {
            repository.markChatAsRead(contactId)
        }
    }

    fun closeChat() {
        _selectedChatId.value = null
    }

    fun sendMessage(text: String, type: MessageType = MessageType.TEXT, extra: String? = null) {
        val chatId = _selectedChatId.value ?: return
        if (text.isBlank() && extra == null) return
        viewModelScope.launch {
            repository.sendMessage(chatId, text, type, extra)
        }
    }

    fun reactToMessage(messageId: Long, emoji: String?) {
        viewModelScope.launch {
            repository.updateReaction(messageId, emoji)
        }
    }

    fun toggleStarMessage(messageId: Long) {
        viewModelScope.launch {
            repository.toggleStar(messageId)
        }
    }

    fun deleteMessage(messageId: Long) {
        viewModelScope.launch {
            repository.deleteMessage(messageId)
        }
    }

    fun clearChat(chatId: String) {
        viewModelScope.launch {
            repository.clearChat(chatId)
        }
    }

    fun startCall(contact: ContactEntity, isVideo: Boolean) {
        _activeCall.value = ActiveCallInfo(contact = contact, isVideo = isVideo, isConnected = false)
        callTimerJob?.cancel()
        callTimerJob = viewModelScope.launch {
            // Ringing delay
            delay(2000)
            _activeCall.value = _activeCall.value?.copy(isConnected = true)
            while (true) {
                delay(1000)
                val current = _activeCall.value ?: break
                _activeCall.value = current.copy(durationSeconds = current.durationSeconds + 1)
            }
        }
    }

    fun toggleMuteCall() {
        val current = _activeCall.value ?: return
        _activeCall.value = current.copy(isMuted = !current.isMuted)
    }

    fun endCall() {
        callTimerJob?.cancel()
        callTimerJob = null
        _activeCall.value = null
    }

    fun openNewChatDialog() {
        _showNewChatDialog.value = true
    }

    fun closeNewChatDialog() {
        _showNewChatDialog.value = false
    }

    fun createNewContactOrGroup(name: String, handle: String, status: String, isGroup: Boolean) {
        viewModelScope.launch {
            repository.addNewContact(name, handle, status, isGroup)
            _showNewChatDialog.value = false
        }
    }

    fun openStory(story: StoryItem) {
        _showStoryPreview.value = story
    }

    fun closeStory() {
        _showStoryPreview.value = null
    }

    fun updateProfile(name: String, bio: String, phone: String, isOnline: Boolean, wallpaperTheme: String) {
        viewModelScope.launch {
            repository.updateProfile(name, bio, phone, isOnline, wallpaperTheme)
        }
    }
}
