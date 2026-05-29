package com.example.demo.ui.graph

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.demo.AppViewModelFactory
import com.example.demo.R
import com.example.demo.view.KnowledgeGraphView
import com.example.demo.view.RadarChartView
import com.example.demo.viewmodel.GraphViewModel

class GraphFragment : Fragment() {

    private lateinit var viewModel: GraphViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_graph, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, AppViewModelFactory())[GraphViewModel::class.java]

        val graphView = view.findViewById<KnowledgeGraphView>(R.id.knowledge_graph)
        val radarView = view.findViewById<RadarChartView>(R.id.radar_chart)

        // Populate views with data from ViewModel
        graphView.post {
            graphView.nodes = viewModel.conceptNodes
        }

        radarView.post {
            radarView.dimensions = viewModel.radarDimensions.map { d ->
                RadarChartView.Dimension(d.name, d.score)
            }
        }
    }
}
