package com.example.demo.model

data class PracticeSession(
    val id: Long = System.currentTimeMillis(),
    val subject: String,
    val questionText: String,
    val steps: List<String>,
    val diagnosis: String = "",
    val score: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
