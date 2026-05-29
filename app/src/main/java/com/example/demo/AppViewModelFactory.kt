package com.example.demo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.demo.viewmodel.*

class AppViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            DashboardViewModel::class.java -> DashboardViewModel(
                ServiceLocator.todoRepository,
                ServiceLocator.progressRepository
            )
            CourseViewModel::class.java -> CourseViewModel(
                ServiceLocator.practiceRepository
            )
            ChatViewModel::class.java -> ChatViewModel(
                ServiceLocator.chatRepository,
                ServiceLocator.mockAi
            )
            LabViewModel::class.java -> LabViewModel(
                ServiceLocator.progressRepository,
                ServiceLocator.mockAi
            )
            GraphViewModel::class.java -> GraphViewModel(
                ServiceLocator.progressRepository
            )
            ProfileViewModel::class.java -> ProfileViewModel(
                ServiceLocator.progressRepository
            )
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        } as T
    }
}
