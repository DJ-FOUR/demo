package com.example.demo.viewmodel

import androidx.lifecycle.ViewModel
import com.example.demo.model.UserProfile
import com.example.demo.repository.ProgressRepository

class ProfileViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    var onDataChanged: (() -> Unit)? = null

    val profile: UserProfile
        get() = UserProfile(
            totalStudyMinutes = progressRepository.getStudyMinutes(),
            totalPracticeCount = 47,
            streakDays = progressRepository.getStreakDays()
        )

    val studyMinutes: Int get() = progressRepository.getStudyMinutes()
    val streakDays: Int get() = progressRepository.getStreakDays()
}
