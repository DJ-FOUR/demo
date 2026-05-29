package com.example.demo.data.local

import com.example.demo.model.ConceptNode
import com.example.demo.model.KnowledgePoint
import com.example.demo.model.RadarDimension
import com.example.demo.repository.ProgressRepository
import com.example.demo.repository.TodoRepository

class LocalProgressRepository(
    private val todoRepository: TodoRepository
) : ProgressRepository {

    override fun getMasteryPercent(): Float = todoRepository.getCompletionPercent()

    override fun getRadarDimensions(): List<RadarDimension> = listOf(
        RadarDimension("记忆力", 82f),
        RadarDimension("逻辑力", 91f),
        RadarDimension("计算力", 88f),
        RadarDimension("专注度", 78f),
        RadarDimension("创新力", 85f),
        RadarDimension("坚持度", 92f)
    )

    override fun getConceptNodes(): List<ConceptNode> = listOf(
        ConceptNode("矩阵运算", 85, 0.3f, 0.2f),
        ConceptNode("行列式", 72, 0.7f, 0.2f),
        ConceptNode("特征值", 90, 0.5f, 0.5f),
        ConceptNode("线性变换", 60, 0.2f, 0.75f),
        ConceptNode("奇异值分解", -1, 0.8f, 0.75f)
    )

    override fun getWeakPoints(): List<KnowledgePoint> = listOf(
        KnowledgePoint("特征值与特征向量", "2天前", 45),
        KnowledgePoint("英语虚拟语气", "昨天", 38),
        KnowledgePoint("线性变换", "今天", 52)
    )

    override fun getStudyMinutes(): Int = 1280

    override fun getStreakDays(): Int = 12
}
