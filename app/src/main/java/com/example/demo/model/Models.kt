package com.example.demo.model

data class TodoItem(
    val id: Long = System.currentTimeMillis(),
    val text: String,
    val isCompleted: Boolean = false
)

data class ChatMessage(
    val id: Long = System.currentTimeMillis(),
    val content: String,
    val isUser: Boolean,
    val timestamp: String = ""
)

data class KnowledgePoint(
    val name: String,
    val lastErrorTime: String,
    val masteryPercent: Int = 0
)

data class RadarDimension(
    val name: String,
    val score: Float // 0-100
)

data class ConceptNode(
    val name: String,
    val mastery: Int, // 0-100, -1 = locked
    val x: Float, // relative position 0-1
    val y: Float
)

data class QuestionStep(
    val index: Int,
    val content: String = ""
)
