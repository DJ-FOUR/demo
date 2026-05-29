package com.example.demo.repository

import com.example.demo.model.ConceptNode
import com.example.demo.model.KnowledgePoint
import com.example.demo.model.RadarDimension

interface ProgressRepository {
    fun getMasteryPercent(): Float
    fun getRadarDimensions(): List<RadarDimension>
    fun getConceptNodes(): List<ConceptNode>
    fun getWeakPoints(): List<KnowledgePoint>
    fun getStudyMinutes(): Int
    fun getStreakDays(): Int
}
