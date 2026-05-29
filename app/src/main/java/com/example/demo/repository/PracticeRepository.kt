package com.example.demo.repository

import com.example.demo.model.PracticeSession

interface PracticeRepository {
    fun getSessions(): List<PracticeSession>
    fun submitPractice(
        questionText: String,
        subject: String,
        steps: List<String>
    ): PracticeSession
}
