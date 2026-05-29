package com.example.demo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo.repository.ProgressRepository
import com.example.demo.service.MockAiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LabViewModel(
    private val progressRepository: ProgressRepository,
    private val mockAi: MockAiService
) : ViewModel() {

    enum class State { CONTENT, LOADING, EMPTY }

    var onStateChanged: ((State) -> Unit)? = null

    var currentQuestion: MockAiService.GeneratedQuestion? = null
        private set
    var deepAnalysisText: String = ""
        private set

    private var isGenerating = false

    fun generate() {
        if (isGenerating) return
        isGenerating = true
        onStateChanged?.invoke(State.LOADING)

        viewModelScope.launch {
            delay(1500)

            val weakPoints = progressRepository.getWeakPoints().map { it.name }
            val question = mockAi.generateQuestion(weakPoints)
            currentQuestion = question
            deepAnalysisText = buildDeepAnalysis(question)

            isGenerating = false
            onStateChanged?.invoke(State.CONTENT)
        }
    }

    private fun buildDeepAnalysis(q: MockAiService.GeneratedQuestion): String {
        val points = when (q.subject) {
            "特征值与特征向量" -> "特征方程、特征空间与对角化、相似矩阵"
            "行列式" -> "展开定理、行列式性质、Cramer法则"
            "矩阵运算" -> "矩阵乘法、逆矩阵求解、初等变换"
            "英语虚拟语气" -> "三种时间类型、wish/as if句型、should用法"
            else -> "基础概念回顾、相关定理链接、拓展应用"
        }
        return "💡 ${q.hint}\n📚 关联：$points\n⏱ 预计 15-20 分钟"
    }

    fun showEmpty() {
        onStateChanged?.invoke(State.EMPTY)
    }
}
