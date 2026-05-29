package com.example.demo.model

data class UserProfile(
    val nickname: String = "学术研究员学友",
    val email: String = "xueyou@ai-academy.cn",
    val avatarUrl: String = "",
    val totalStudyMinutes: Int = 1280,
    val totalPracticeCount: Int = 47,
    val streakDays: Int = 12
)
