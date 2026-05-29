package com.example.demo.ui.course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.demo.AppViewModelFactory
import com.example.demo.R
import com.example.demo.view.DrawingCanvasView
import com.example.demo.viewmodel.CourseViewModel

class CourseFragment : Fragment() {

    private lateinit var viewModel: CourseViewModel
    private val stepInputs = mutableListOf<EditText>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_course, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, AppViewModelFactory())[CourseViewModel::class.java]
        viewModel.initDefaultStep()

        val stepsContainer = view.findViewById<LinearLayout>(R.id.steps_container)
        val hintPanel = view.findViewById<LinearLayout>(R.id.hint_panel)
        val canvas = view.findViewById<DrawingCanvasView>(R.id.drawing_canvas)
        val questionText = view.findViewById<TextView>(R.id.question_text)
        val submitBtn = view.findViewById<TextView>(R.id.submit_btn)
        val aiHintBtn = view.findViewById<TextView>(R.id.ai_hint_btn)

        // Set question
        questionText.text = viewModel.questionText

        // Build initial step inputs
        rebuildStepInputs(stepsContainer)

        view.findViewById<TextView>(R.id.add_step_btn).setOnClickListener {
            viewModel.addStep()
            addStepInput(stepsContainer, viewModel.stepContents.size - 1)
        }

        view.findViewById<ImageView>(R.id.course_back).setOnClickListener {
            activity?.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_nav)
                ?.selectedItemId = R.id.nav_home
        }

        aiHintBtn.setOnClickListener {
            if (viewModel.isSubmitted) {
                // Reset after submission
                viewModel.reset()
                stepInputs.clear()
                stepsContainer.removeAllViews()
                rebuildStepInputs(stepsContainer)
                submitBtn.text = getString(R.string.submit_answer)
                aiHintBtn.text = getString(R.string.ai_hint)
                questionText.text = viewModel.questionText
                hintPanel.visibility = View.GONE
            } else {
                hintPanel.visibility = if (hintPanel.visibility == View.GONE) View.VISIBLE else View.GONE
            }
        }

        submitBtn.setOnClickListener {
            if (!viewModel.isSubmitted) {
                // Sync step inputs to ViewModel
                for (i in stepInputs.indices) {
                    viewModel.updateStep(i, stepInputs[i].text.toString().trim())
                }
                val session = viewModel.submit()

                submitBtn.text = "查看诊断报告"
                aiHintBtn.text = "重新作答"

                // Show diagnosis in question text area
                questionText.text = session.diagnosis
            }
        }

        view.findViewById<TextView>(R.id.erase_btn).setOnClickListener {
            canvas.clearCanvas()
        }
    }

    private fun rebuildStepInputs(container: LinearLayout) {
        container.removeAllViews()
        stepInputs.clear()
        for (i in viewModel.stepContents.indices) {
            addStepInput(container, i)
        }
    }

    private fun addStepInput(container: LinearLayout, index: Int) {
        val stepLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = 8.dpToPx }
        }

        val label = TextView(requireContext()).apply {
            text = "步骤 ${index + 1}"
            textSize = 12f
            setTextColor(resources.getColor(R.color.text_secondary, null))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { marginEnd = 12.dpToPx }
        }

        val input = EditText(requireContext()).apply {
            hint = "输入解答步骤…"
            textSize = 14f
            setBackgroundColor(resources.getColor(R.color.surface_variant, null))
            setPadding(16.dpToPx, 12.dpToPx, 16.dpToPx, 12.dpToPx)
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
            if (index < viewModel.stepContents.size) {
                setText(viewModel.stepContents[index])
            }
        }

        stepLayout.addView(label)
        stepLayout.addView(input)
        container.addView(stepLayout)
        stepInputs.add(input)
    }

    private val Int.dpToPx: Int
        get() = (this * resources.displayMetrics.density).toInt()
}
