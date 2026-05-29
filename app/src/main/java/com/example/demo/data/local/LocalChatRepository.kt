package com.example.demo.data.local

import com.example.demo.model.ChatMessage
import com.example.demo.repository.ChatRepository

class LocalChatRepository : ChatRepository {

    private val messages = mutableListOf(
        ChatMessage(1, "你好！我是你的 AI 专属学术导师，有任何学习问题随时问我。", false, "10:30"),
        ChatMessage(2, "老师，特征值和特征向量的几何意义是什么？", true, "10:31"),
        ChatMessage(3, "特征向量表示线性变换中方向不变的特殊向量，而特征值表示该向量被拉伸或压缩的比例。几何上，如果把矩阵想象成空间的变形，特征向量就是变形后仍保持在同一直线上的方向。", false, "10:32")
    )
    private var nextId = 4L

    override fun getMessages(): List<ChatMessage> = messages.toList()

    override fun sendMessage(content: String): ChatMessage {
        val msg = ChatMessage(id = nextId++, content = content, isUser = true, timestamp = "刚刚")
        messages.add(msg)
        return msg
    }

    override fun addMessage(message: ChatMessage) {
        messages.add(message)
    }

    override fun clearMessages() {
        messages.clear()
    }
}
