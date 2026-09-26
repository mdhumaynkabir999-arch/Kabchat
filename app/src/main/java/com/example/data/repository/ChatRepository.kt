package com.example.data.repository

import com.example.data.local.ContactDao
import com.example.data.local.MessageDao
import com.example.data.model.ContactEntity
import com.example.data.model.MessageEntity
import com.example.data.model.MessageStatus
import com.example.data.model.MessageType
import com.example.data.model.StoryItem
import com.example.data.model.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatRepository(
    private val contactDao: ContactDao,
    private val messageDao: MessageDao,
    private val scope: CoroutineScope
) {
    val allContacts: Flow<List<ContactEntity>> = contactDao.getAllContacts()
    val directContacts: Flow<List<ContactEntity>> = contactDao.getDirectContacts()
    val groups: Flow<List<ContactEntity>> = contactDao.getGroups()
    val onlineContacts: Flow<List<ContactEntity>> = contactDao.getOnlineContacts()
    val userProfile: Flow<UserProfile?> = contactDao.getUserProfile()

    // Track active typing indicators for each chat
    private val _typingStatus = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val typingStatus: StateFlow<Map<String, Boolean>> = _typingStatus.asStateFlow()

    // Active stories / Status items
    private val _stories = MutableStateFlow<List<StoryItem>>(
        listOf(
            StoryItem("1", "তামিম ইকবাল", "👨‍💼", 0xFF0D9488L, false, "১০ মিনিট আগে", "অফিস আড্ডা ☕"),
            StoryItem("2", "সায়মা আহমেদ", "👩‍🎨", 0xFFE11D48L, false, "২৫ মিনিট আগে", "নতুন ক্যানভাস আর্ট 🎨"),
            StoryItem("3", "রাহুল দে", "👨‍💻", 0xFF2563EBL, false, "১ ঘণ্টা আগে", "হ্যাকথন প্রিপারেশন 🚀"),
            StoryItem("4", "প্রিয়তা নূর", "👩‍⚕️", 0xFF7C3AEDL, true, "৩ ঘণ্টা আগে", "বৃষ্টিভেজা বিকেল 🌧️"),
            StoryItem("5", "আসিফ মাহমুদ", "🏃‍♂️", 0xFFD97706L, false, "৪ ঘণ্টা আগে", "মর্নিং ওয়াক 🏃")
        )
    )
    val stories: StateFlow<List<StoryItem>> = _stories.asStateFlow()

    init {
        scope.launch(Dispatchers.IO) {
            seedInitialDataIfNeeded()
        }
    }

    private suspend fun seedInitialDataIfNeeded() {
        if (contactDao.getContactsCount() == 0) {
            val defaultProfile = UserProfile(
                id = "me",
                displayName = "হুমায়ুন কবির",
                username = "@humayun_k",
                bio = "আলাপ অ্যাপে সক্রিয় 🌟 | সবসময় মেসেজে কানেক্টেড",
                phone = "+880 1712-345678",
                avatarEmoji = "👨‍💻",
                avatarColor = 0xFF0F766EL,
                isOnline = true,
                isBangla = true,
                wallpaperTheme = "emerald"
            )
            contactDao.saveUserProfile(defaultProfile)

            val now = System.currentTimeMillis()
            val initialContacts = listOf(
                ContactEntity(
                    id = "tamim",
                    name = "তামিম ইকবাল",
                    avatarColor = 0xFF0D9488L,
                    avatarEmoji = "👨‍💼",
                    statusMessage = "আলাপেই আছি, যেকোনো প্রয়োজনে মেসেজ দিন",
                    isOnline = true,
                    lastSeen = "অনলাইন",
                    isGroup = false,
                    phoneOrHandle = "+880 1812-445566",
                    unreadCount = 2,
                    lastMessageText = "আজকের কাজের আপডেটটা পাঠাও ভাই।",
                    lastMessageTime = now - 1000 * 60 * 3,
                    isPinned = true
                ),
                ContactEntity(
                    id = "sayma",
                    name = "সায়মা আহমেদ",
                    avatarColor = 0xFFE11D48L,
                    avatarEmoji = "👩‍🎨",
                    statusMessage = "চিত্রশিল্প ও কফির প্রেম ☕🎨",
                    isOnline = true,
                    lastSeen = "অনলাইন",
                    isGroup = false,
                    phoneOrHandle = "+880 1719-887766",
                    unreadCount = 0,
                    lastMessageText = "নতুন ডিজাইন ফ্রেমগুলো কেমন লাগল?",
                    lastMessageTime = now - 1000 * 60 * 18,
                    isPinned = true
                ),
                ContactEntity(
                    id = "group_friends",
                    name = "বন্ধুমহল 🚀",
                    avatarColor = 0xFFF59E0BL,
                    avatarEmoji = "🏕️",
                    statusMessage = "সাপ্তাহিক আড্ডা ও ভ্রমণ পরিকল্পনা",
                    isOnline = true,
                    lastSeen = "৫ জন সদস্য",
                    isGroup = true,
                    membersCount = 6,
                    phoneOrHandle = "গ্রুপ চ্যাট",
                    unreadCount = 3,
                    lastMessageText = "রাহুল: এই শুক্রবারে মাওয়া ঘাটে যাই চলো!",
                    lastMessageTime = now - 1000 * 60 * 5,
                    isPinned = true
                ),
                ContactEntity(
                    id = "rahul",
                    name = "রাহুল দে",
                    avatarColor = 0xFF2563EBL,
                    avatarEmoji = "👨‍💻",
                    statusMessage = "Kotlin & Jetpack Compose Lover 💻",
                    isOnline = true,
                    lastSeen = "অনলাইন",
                    isGroup = false,
                    phoneOrHandle = "+880 1911-223344",
                    unreadCount = 0,
                    lastMessageText = "আলাপ অ্যাপের ইউজার ইন্টারফেস অনেক চমৎকার হয়েছে!",
                    lastMessageTime = now - 1000 * 60 * 45,
                    isPinned = false
                ),
                ContactEntity(
                    id = "bot_aalap",
                    name = "আলাপ AI সহায়ক 🤖",
                    avatarColor = 0xFF4F46E5L,
                    avatarEmoji = "🤖",
                    statusMessage = "সবসময় আপনার সেবায় প্রস্তুত স্মার্ট অ্যাসিস্ট্যান্ট",
                    isOnline = true,
                    lastSeen = "অনলাইন",
                    isGroup = false,
                    phoneOrHandle = "অফিসিয়াল বট",
                    unreadCount = 0,
                    lastMessageText = "স্বাগতম 'আলাপ' চ্যাট অ্যাপে! আপনার দিনটি শুভ হোক।",
                    lastMessageTime = now - 1000 * 60 * 120,
                    isPinned = false
                ),
                ContactEntity(
                    id = "priyota",
                    name = "প্রিয়তা নূর",
                    avatarColor = 0xFF7C3AEDL,
                    avatarEmoji = "👩‍⚕️",
                    statusMessage = "হসপিটাল ডিউটিতে ব্যস্ত 🩺",
                    isOnline = false,
                    lastSeen = "২০ মিনিট আগে",
                    isGroup = false,
                    phoneOrHandle = "+880 1515-998877",
                    unreadCount = 0,
                    lastMessageText = "সন্ধ্যায় ফ্রি হলে একটা কল দিও।",
                    lastMessageTime = now - 1000 * 60 * 200,
                    isPinned = false
                ),
                ContactEntity(
                    id = "asif",
                    name = "আসিফ মাহমুদ",
                    avatarColor = 0xFF059669L,
                    avatarEmoji = "🏃‍♂️",
                    statusMessage = "ম্যারাথন প্রস্তুতি চলছে 🏅",
                    isOnline = true,
                    lastSeen = "অনলাইন",
                    isGroup = false,
                    phoneOrHandle = "+880 1616-332211",
                    unreadCount = 0,
                    lastMessageText = "কাল সকালে ধানমন্ডি লেকে দৌড়াতে আসছিস?",
                    lastMessageTime = now - 1000 * 60 * 350,
                    isPinned = false
                )
            )
            contactDao.insertContacts(initialContacts)

            // Seed messages for initial conversation
            val seedMessages = listOf(
                // Tamim
                MessageEntity(
                    chatId = "tamim",
                    senderId = "tamim",
                    senderName = "তামিম ইকবাল",
                    text = "শুভ অপরাহ্ন! কেমন আছো ভাই?",
                    timestamp = now - 1000 * 60 * 15,
                    status = MessageStatus.READ.name,
                    type = MessageType.TEXT.name
                ),
                MessageEntity(
                    chatId = "tamim",
                    senderId = "me",
                    senderName = "আমি",
                    text = "আলহামদুলিল্লাহ ভাই, ভালো আছি। তোমার খবর কি?",
                    timestamp = now - 1000 * 60 * 10,
                    status = MessageStatus.READ.name,
                    type = MessageType.TEXT.name
                ),
                MessageEntity(
                    chatId = "tamim",
                    senderId = "tamim",
                    senderName = "তামিম ইকবাল",
                    text = "আজকের কাজের আপডেটটা পাঠাও ভাই।",
                    timestamp = now - 1000 * 60 * 3,
                    status = MessageStatus.DELIVERED.name,
                    type = MessageType.TEXT.name
                ),
                // Sayma
                MessageEntity(
                    chatId = "sayma",
                    senderId = "sayma",
                    senderName = "সায়মা আহমেদ",
                    text = "আমি একটা নতুন আর্ট প্রজেক্ট শুরু করেছি 🎨",
                    timestamp = now - 1000 * 60 * 30,
                    status = MessageStatus.READ.name,
                    type = MessageType.TEXT.name
                ),
                MessageEntity(
                    chatId = "sayma",
                    senderId = "sayma",
                    senderName = "সায়মা আহমেদ",
                    text = "নতুন ডিজাইন ফ্রেমগুলো কেমন লাগল?",
                    timestamp = now - 1000 * 60 * 18,
                    status = MessageStatus.READ.name,
                    type = MessageType.TEXT.name
                ),
                // Group Friends
                MessageEntity(
                    chatId = "group_friends",
                    senderId = "asif",
                    senderName = "আসিফ",
                    text = "বন্ধুরা, এই ছুটির দিনে প্ল্যান কি?",
                    timestamp = now - 1000 * 60 * 12,
                    status = MessageStatus.READ.name,
                    type = MessageType.TEXT.name
                ),
                MessageEntity(
                    chatId = "group_friends",
                    senderId = "sayma",
                    senderName = "সায়মা",
                    text = "আমার তো কোথাও ঘুরতে যেতে ইচ্ছে করছে!",
                    timestamp = now - 1000 * 60 * 8,
                    status = MessageStatus.READ.name,
                    type = MessageType.TEXT.name
                ),
                MessageEntity(
                    chatId = "group_friends",
                    senderId = "rahul",
                    senderName = "রাহুল দে",
                    text = "রাহুল: এই শুক্রবারে মাওয়া ঘাটে যাই চলো!",
                    timestamp = now - 1000 * 60 * 5,
                    status = MessageStatus.READ.name,
                    type = MessageType.TEXT.name
                ),
                // Bot Aalap
                MessageEntity(
                    chatId = "bot_aalap",
                    senderId = "bot_aalap",
                    senderName = "আলাপ AI সহায়ক",
                    text = "স্বাগতম 'আলাপ' চ্যাট অ্যাপে! যেকোনো প্রশ্ন থাকলে আমাকে সরাসরি জানাতে পারেন।",
                    timestamp = now - 1000 * 60 * 120,
                    status = MessageStatus.READ.name,
                    type = MessageType.TEXT.name
                )
            )
            messageDao.insertMessages(seedMessages)
        }
    }

    fun getMessagesForChat(chatId: String): Flow<List<MessageEntity>> {
        return messageDao.getMessagesForChat(chatId)
    }

    fun getContactById(contactId: String): Flow<ContactEntity?> {
        return contactDao.getContactById(contactId)
    }

    suspend fun markChatAsRead(chatId: String) {
        contactDao.markChatAsRead(chatId)
    }

    suspend fun sendMessage(
        chatId: String,
        text: String,
        type: MessageType = MessageType.TEXT,
        mediaExtra: String? = null
    ) {
        val now = System.currentTimeMillis()
        val userMsg = MessageEntity(
            chatId = chatId,
            senderId = "me",
            senderName = "হুমায়ুন কবির",
            text = text,
            timestamp = now,
            status = MessageStatus.SENT.name,
            type = type.name,
            mediaUrlOrExtra = mediaExtra
        )
        messageDao.insertMessage(userMsg)
        contactDao.updateLastMessage(chatId, text, now)

        // Trigger intelligent automated response simulation
        triggerSimulatedResponse(chatId, text, type)
    }

    private fun triggerSimulatedResponse(chatId: String, userText: String, type: MessageType) {
        scope.launch(Dispatchers.IO) {
            // Delay to simulate network & typing
            delay(1000)
            _typingStatus.value = _typingStatus.value + (chatId to true)
            delay(1800)
            _typingStatus.value = _typingStatus.value - chatId

            val replyText = generateContextualReply(chatId, userText, type)
            val replyTime = System.currentTimeMillis()

            val replyMsg = MessageEntity(
                chatId = chatId,
                senderId = chatId,
                senderName = getSenderNameForChat(chatId),
                text = replyText,
                timestamp = replyTime,
                status = MessageStatus.READ.name,
                type = MessageType.TEXT.name
            )

            messageDao.insertMessage(replyMsg)
            contactDao.updateLastMessage(chatId, replyText, replyTime)
        }
    }

    private fun getSenderNameForChat(chatId: String): String {
        return when (chatId) {
            "tamim" -> "তামিম ইকবাল"
            "sayma" -> "সায়মা আহমেদ"
            "rahul" -> "রাহুল দে"
            "priyota" -> "প্রিয়তা নূর"
            "asif" -> "আসিফ মাহমুদ"
            "group_friends" -> "তানভীর (গ্রুপ মেম্বার)"
            "bot_aalap" -> "আলাপ AI সহায়ক"
            else -> "অনলাইন বন্ধু"
        }
    }

    private fun generateContextualReply(chatId: String, text: String, type: MessageType): String {
        val lower = text.lowercase().trim()

        if (type == MessageType.VOICE) {
            return "দারুণ ভয়েস মেসেজ! স্পষ্ট শুনতে পেয়েছি। আমি একটু পরেই ফ্রি হয়ে বিস্তারিত বলছি।"
        }
        if (type == MessageType.IMAGE) {
            return "ছবিটি অনেক চমৎকার হয়েছে! দারুণ ক্লিকের তারিফ করতেই হয় 📸✨"
        }
        if (type == MessageType.LOCATION) {
            return "লোকেশন পেয়েছি! আমি কাছাকাছি আছি, পৌঁছাতে ১৫ মিনিট লাগবে।"
        }

        if (chatId == "bot_aalap") {
            return when {
                lower.contains("কেমন") || lower.contains("how") -> "আমি আলাপ এআই সহায়ক, চমৎকার আছি! আপনাকে কীভাবে সাহায্য করতে পারি?"
                lower.contains("আলাপ") || lower.contains("app") -> "আলাপ হলো একটি দ্রুত, সুরক্ষিত ও আধুনিক বাংলা অনলাইন মেসেজিং প্ল্যাটফর্ম। এখানে আপনি টেক্সট, ভয়েস, ছবি ও গ্রুপে আড্ডা দিতে পারবেন!"
                lower.contains("ধন্যবাদ") || lower.contains("thank") -> "আপনাকেও অসংখ্য ধন্যবাদ! আলাপ-এর সাথে যুক্ত থাকুন।"
                lower.contains("ফিচার") || lower.contains("feature") -> "আলাপের ফিচারসমূহ: ১. রিয়েল-টাইম চ্যাট ২. অনলাইন ফ্রেন্ডস ট্র্যাকিং ৩. গ্রুপ চ্যাট ৪. ভয়েস নোটস ৫. ফটো শেয়ারিং ও রিঅ্যাকশন।"
                else -> "ধন্যবাদ আপনার বার্তার জন্য! আলাপ সর্বদা আপনার সেবায় সক্রিয় রয়েছে।"
            }
        }

        if (chatId == "group_friends") {
            val groupReplies = listOf(
                "হ্যাঁ ভাই, আমি কিন্তু যাবই! গাড়ি নিয়ে কে যাচ্ছিস?",
                "দারুণ প্রস্তাব! সবাই মিলে অনেক দিন পর আড্ডা হবে 🔥",
                "আমি ব্যাকপ্যাক গুছিয়ে নিচ্ছি। কয়টায় বের হবো?",
                "ছবি তোলার জন্য ক্যামেরা রেডি রাখিস রাহুল!"
            )
            return groupReplies.random()
        }

        return when {
            lower.contains("সালাম") || lower.contains("salam") || lower.contains("হ্যালো") || lower.contains("হাই") || lower.contains("hi") || lower.contains("hello") ->
                "ওয়ালাইকুম আসসালাম! কেমন আছো বন্ধু? দিনকাল কেমন কাটছে?"
            lower.contains("কেমন") || lower.contains("how are") ->
                "আলহামদুলিল্লাহ অনেক ভালো আছি! তোমার কাজ কেমন চলছে?"
            lower.contains("কোথায়") || lower.contains("where") ->
                "আমি এখন ধানমন্ডিতে একটা কাজে আছি। তুমি কি বাসায় নাকি অফিসে?"
            lower.contains("কাজ") || lower.contains("update") || lower.contains("প্রজেক্ট") ->
                "কাজ পুরোদমে এগোচ্ছে ভাই। ইনশাআল্লাহ সন্ধ্যার মধ্যেই কমপ্লিট করে পাঠিয়ে দিচ্ছি।"
            lower.contains("দেখা") || lower.contains("meet") ->
                "অবশ্যই! কাল বিকেলে কি এক কাপ কফি খাওয়া যায়?"
            lower.contains("চা") || lower.contains("কফি") || lower.contains("coffee") ->
                "চলো যাই! গরম এক কাপ চা/কফি খাওয়ার দারুণ আবহাওয়া এখন ☕"
            lower.contains("শুভ") || lower.contains("good") ->
                "তোমাকেও অনেক অনেক শুভকামনা ও ভালোবাসা! দিনটি চমৎকার কাটুক। ✨"
            lower.contains("ঠিক আছে") || lower.contains("ok") || lower.contains("okay") ->
                "পারফেক্ট! কোনো আপডেট থাকলে অবশ্যই আমাকে জানিও।"
            else -> {
                val genericReplies = listOf(
                    "একদম ঠিক কথা বলেছো! আমি পুরোপুরি একমত 👍",
                    "হ্যাঁ ভাই, বিষয়টা বেশ দারুণ। একটু পরেই তোমায় বিস্তারিত জানাচ্ছি।",
                    "খুব ভালো লাগলো তোমার সাথে কথা বলে! 😊",
                    "আমি একটু কাজে ছিলাম, এখনই তোমার মেসেজ দেখলাম।"
                )
                genericReplies.random()
            }
        }
    }

    suspend fun updateReaction(messageId: Long, reaction: String?) {
        messageDao.updateReaction(messageId, reaction)
    }

    suspend fun toggleStar(messageId: Long) {
        messageDao.toggleStar(messageId)
    }

    suspend fun deleteMessage(messageId: Long) {
        messageDao.deleteMessage(messageId)
    }

    suspend fun clearChat(chatId: String) {
        messageDao.clearChat(chatId)
    }

    suspend fun addNewContact(
        name: String,
        phoneOrHandle: String,
        status: String,
        isGroup: Boolean = false
    ) {
        val id = "contact_${System.currentTimeMillis()}"
        val emojis = listOf("😎", "🥳", "💼", "🚀", "🌟", "🔥", "🎨", "⚽")
        val colors = listOf(0xFF0D9488L, 0xFFE11D48L, 0xFF2563EBL, 0xFF7C3AEDL, 0xFFD97706L)
        val newContact = ContactEntity(
            id = id,
            name = name,
            avatarColor = colors.random(),
            avatarEmoji = if (isGroup) "👥" else emojis.random(),
            statusMessage = status.ifBlank { "আলাপ ব্যবহারকারী" },
            isOnline = true,
            lastSeen = "অনলাইন",
            isGroup = isGroup,
            membersCount = if (isGroup) 4 else 1,
            phoneOrHandle = phoneOrHandle,
            unreadCount = 0,
            lastMessageText = if (isGroup) "নতুন গ্রুপ তৈরি করা হয়েছে" else "আলাপ-এ স্বাগতম!",
            lastMessageTime = System.currentTimeMillis()
        )
        contactDao.insertContact(newContact)
    }

    suspend fun updateProfile(
        displayName: String,
        bio: String,
        phone: String,
        isOnline: Boolean,
        wallpaperTheme: String
    ) {
        val current = UserProfile(
            id = "me",
            displayName = displayName,
            username = "@${displayName.lowercase().replace(" ", "_")}",
            bio = bio,
            phone = phone,
            avatarEmoji = "👨‍💻",
            avatarColor = 0xFF0F766EL,
            isOnline = isOnline,
            isBangla = true,
            wallpaperTheme = wallpaperTheme
        )
        contactDao.saveUserProfile(current)
    }
}
