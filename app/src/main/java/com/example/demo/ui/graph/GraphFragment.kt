package com.example.demo.ui.graph

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.demo.R
import com.example.demo.view.KnowledgeGraphView
import com.example.demo.view.RadarChartView

class GraphFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_graph, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val graphView = view.findViewById<KnowledgeGraphView>(R.id.knowledge_graph)
        val radarView = view.findViewById<RadarChartView>(R.id.radar_chart)

        // Custom views are already initialized via XML with default data
        // Graph nodes and radar dimensions can be updated here if needed
        graphView.post {
            // Ready
        }

        radarView.post {
            // Ready
        }
    }
}
