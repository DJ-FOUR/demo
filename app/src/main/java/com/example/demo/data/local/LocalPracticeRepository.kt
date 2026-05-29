package com.example.demo.data.local

import com.example.demo.model.PracticeSession
import com.example.demo.repository.PracticeRepository
import com.example.demo.service.MockAiService

class LocalPracticeRepository(
    private val mockAi: MockAiService
) : PracticeRepository {

    private val sessions = mutableListOf<PracticeSession>()

    override fun getSessions(): List<PracticeSession> = sessions.toList()

    override fun submitPractice(
        questionText: String,
        subject: String,
        steps: List<String>
    ): PracticeSession {
        val diagnosis = mockAi.diagnose(subject, questionText, steps)
        val score = mockAi.scorePractice(steps)
        val session = PracticeSession(
            subject = subject,
            questionText = questionText,
            steps = steps,
            diagnosis = diagnosis,
            score = score
        )
        sessions.add(session)
        return session
    }
}
