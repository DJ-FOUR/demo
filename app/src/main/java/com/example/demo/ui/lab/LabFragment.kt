package com.example.demo.ui.lab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.demo.AppViewModelFactory
import com.example.demo.R
import com.example.demo.viewmodel.LabViewModel

class LabFragment : Fragment() {

    private lateinit var viewModel: LabViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_lab, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, AppViewModelFactory())[LabViewModel::class.java]

        val loadingView = view.findViewById<LinearLayout>(R.id.lab_loading)
        val contentView = view.findViewById<LinearLayout>(R.id.lab_content)
        val emptyView = view.findViewById<LinearLayout>(R.id.lab_empty)
        val regenerateBtn = view.findViewById<TextView>(R.id.regenerate_btn)
        val generateBtn = view.findViewById<TextView>(R.id.generate_btn)

        viewModel.onStateChanged = { state ->
            when (state) {
                LabViewModel.State.CONTENT -> {
                    loadingView.visibility = View.GONE
                    contentView.visibility = View.VISIBLE
                    emptyView.visibility = View.GONE
                    updateContent(view)
                }
                LabViewModel.State.LOADING -> {
                    loadingView.visibility = View.VISIBLE
                    contentView.visibility = View.GONE
                    emptyView.visibility = View.GONE
                }
                LabViewModel.State.EMPTY -> {
                    loadingView.visibility = View.GONE
                    contentView.visibility = View.GONE
                    emptyView.visibility = View.VISIBLE
                }
            }
        }

        viewModel.generate()

        regenerateBtn.setOnClickListener { viewModel.generate() }
        generateBtn.setOnClickListener { viewModel.generate() }
    }

    private fun updateContent(view: View) {
        val q = viewModel.currentQuestion ?: return

        view.findViewById<TextView>(R.id.lab_question_title)?.text = "变式: ${q.subject}"
        view.findViewById<TextView>(R.id.lab_question_content)?.text = q.question
        view.findViewById<TextView>(R.id.lab_hint)?.text = q.hint

        val stepsContainer = view.findViewById<LinearLayout>(R.id.analysis_steps_container) ?: return
        stepsContainer.removeAllViews()
        for (line in viewModel.deepAnalysisText.split("\n")) {
            if (line.isNotBlank()) {
                val tv = TextView(requireContext()).apply {
                    text = line.trim()
                    textSize = 12f
                    setTextColor(resources.getColor(R.color.on_surface, null))
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply { topMargin = 2.dpToPx }
                }
                stepsContainer.addView(tv)
            }
        }
    }

    private val Int.dpToPx: Int
        get() = (this * resources.displayMetrics.density).toInt()
}
