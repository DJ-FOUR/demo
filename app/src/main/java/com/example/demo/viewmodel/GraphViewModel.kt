package com.example.demo.viewmodel

import androidx.lifecycle.ViewModel
import com.example.demo.model.ConceptNode
import com.example.demo.model.KnowledgePoint
import com.example.demo.model.RadarDimension
import com.example.demo.repository.ProgressRepository

class GraphViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    val conceptNodes: List<ConceptNode> get() = progressRepository.getConceptNodes()
    val radarDimensions: List<RadarDimension> get() = progressRepository.getRadarDimensions()
    val weakPoints: List<KnowledgePoint> get() = progressRepository.getWeakPoints()
    val studyMinutes: Int get() = progressRepository.getStudyMinutes()
    val streakDays: Int get() = progressRepository.getStreakDays()
}
