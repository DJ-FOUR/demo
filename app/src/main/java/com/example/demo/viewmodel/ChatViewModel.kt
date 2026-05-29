package com.example.demo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo.model.ChatMessage
import com.example.demo.repository.ChatRepository
import com.example.demo.service.MockAiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val mockAi: MockAiService
) : ViewModel() {

    var onDataChanged: (() -> Unit)? = null
    var onScrollToEnd: (() -> Unit)? = null

    val messages: List<ChatMessage> get() = chatRepository.getMessages()

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        chatRepository.sendMessage(text)
        onDataChanged?.invoke()
        onScrollToEnd?.invoke()

        viewModelScope.launch {
            delay(800 + (text.length * 20).coerceAtMost(1200).toLong())
            val reply = mockAi.generateReply(text)
            val aiMsg = ChatMessage(content = reply, isUser = false, timestamp = "刚刚")
            chatRepository.addMessage(aiMsg)
            onDataChanged?.invoke()
            onScrollToEnd?.invoke()
        }
    }
}
