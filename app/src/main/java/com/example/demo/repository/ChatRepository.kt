package com.example.demo.repository

import com.example.demo.model.ChatMessage

interface ChatRepository {
    fun getMessages(): List<ChatMessage>
    fun sendMessage(content: String): ChatMessage
    fun addMessage(message: ChatMessage)
    fun clearMessages()
}
