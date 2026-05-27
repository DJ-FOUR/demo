package com.example.demo.ui.lab

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.demo.R

class LabFragment : Fragment() {

    private var isGenerating = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_lab, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loadingView = view.findViewById<LinearLayout>(R.id.lab_loading)
        val contentView = view.findViewById<LinearLayout>(R.id.lab_content)
        val emptyView = view.findViewById<LinearLayout>(R.id.lab_empty)
        val regenerateBtn = view.findViewById<TextView>(R.id.regenerate_btn)
        val generateBtn = view.findViewById<TextView>(R.id.generate_btn)

        // Default: show content
        showState(View.VISIBLE, View.GONE, View.GONE)

        regenerateBtn.setOnClickListener {
            if (isGenerating) return@setOnClickListener
            startGeneration(loadingView, contentView, emptyView)
        }

        generateBtn.setOnClickListener {
            if (isGenerating) return@setOnClickListener
            startGeneration(loadingView, contentView, emptyView)
        }
    }

    private fun startGeneration(loading: LinearLayout, content: LinearLayout, empty: LinearLayout) {
        isGenerating = true
        showState(View.GONE, View.VISIBLE, View.GONE)

        Handler(Looper.getMainLooper()).postDelayed({
            isGenerating = false
            showState(View.VISIBLE, View.GONE, View.GONE)
            Toast.makeText(requireContext(), "变式练习已生成！", Toast.LENGTH_SHORT).show()
        }, 2000)
    }

    private fun showState(contentVis: Int, loadingVis: Int, emptyVis: Int) {
        view?.let {
            it.findViewById<LinearLayout>(R.id.lab_content).visibility = contentVis
            it.findViewById<LinearLayout>(R.id.lab_loading).visibility = loadingVis
            it.findViewById<LinearLayout>(R.id.lab_empty).visibility = emptyVis
        }
    }
}
