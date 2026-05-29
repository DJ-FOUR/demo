package com.example.demo.viewmodel

import androidx.lifecycle.ViewModel
import com.example.demo.model.PracticeSession
import com.example.demo.repository.PracticeRepository

class CourseViewModel(
    private val practiceRepository: PracticeRepository
) : ViewModel() {

    var onDataChanged: (() -> Unit)? = null

    var stepContents = mutableListOf<String>()
        private set

    var isSubmitted = false
        private set

    var lastSession: PracticeSession? = null
        private set

    val subject = "线性代数"
    val questionText = "已知矩阵 A = [[3, 1], [0, 2]]，求 A 的特征值和特征向量。"

    fun addStep() {
        stepContents.add("")
        onDataChanged?.invoke()
    }

    fun removeStep(index: Int) {
        if (index in stepContents.indices) {
            stepContents.removeAt(index)
            onDataChanged?.invoke()
        }
    }

    fun updateStep(index: Int, content: String) {
        if (index in stepContents.indices) {
            stepContents[index] = content
        }
    }

    fun submit(): PracticeSession {
        val session = practiceRepository.submitPractice(questionText, subject, stepContents.toList())
        lastSession = session
        isSubmitted = true
        onDataChanged?.invoke()
        return session
    }

    fun reset() {
        stepContents.clear()
        isSubmitted = false
        lastSession = null
        onDataChanged?.invoke()
    }

    fun initDefaultStep() {
        if (stepContents.isEmpty()) {
            stepContents.add("")
        }
    }
}
