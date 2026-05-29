package com.example.demo

import com.example.demo.data.local.LocalChatRepository
import com.example.demo.data.local.LocalPracticeRepository
import com.example.demo.data.local.LocalProgressRepository
import com.example.demo.data.local.LocalTodoRepository
import com.example.demo.repository.ChatRepository
import com.example.demo.repository.PracticeRepository
import com.example.demo.repository.ProgressRepository
import com.example.demo.repository.TodoRepository
import com.example.demo.service.MockAiService

object ServiceLocator {

    val mockAi: MockAiService by lazy { MockAiService() }
    val todoRepository: TodoRepository by lazy { LocalTodoRepository() }
    val chatRepository: ChatRepository by lazy { LocalChatRepository() }
    val practiceRepository: PracticeRepository by lazy { LocalPracticeRepository(mockAi) }
    val progressRepository: ProgressRepository by lazy { LocalProgressRepository(todoRepository) }
}
