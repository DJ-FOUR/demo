package com.example.demo.ui.course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.demo.R
import com.example.demo.view.DrawingCanvasView

class CourseFragment : Fragment() {

    private var stepCount = 0
    private var isSubmitted = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_course, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stepsContainer = view.findViewById<LinearLayout>(R.id.steps_container)
        val hintPanel = view.findViewById<LinearLayout>(R.id.hint_panel)
        val canvas = view.findViewById<DrawingCanvasView>(R.id.drawing_canvas)

        // Add first step
        addStepInput(stepsContainer)

        view.findViewById<TextView>(R.id.add_step_btn).setOnClickListener {
            addStepInput(stepsContainer)
        }

        view.findViewById<ImageView>(R.id.course_back).setOnClickListener {
            activity?.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_nav)
                ?.selectedItemId = R.id.nav_home
        }

        view.findViewById<TextView>(R.id.ai_hint_btn).setOnClickListener {
            hintPanel.visibility = if (hintPanel.visibility == View.GONE) View.VISIBLE else View.GONE
        }

        view.findViewById<TextView>(R.id.submit_btn).setOnClickListener {
            if (!isSubmitted) {
                isSubmitted = true
                Toast.makeText(requireContext(), "答案已提交，AI 正在诊断…", Toast.LENGTH_SHORT).show()

                // Simulate switching to diagnostic view
                view.findViewById<TextView>(R.id.submit_btn).text = "查看诊断报告"
                view.findViewById<TextView>(R.id.ai_hint_btn).text = "重新作答"

                val questionText = view.findViewById<TextView>(R.id.question_text)
                questionText.text = """
                    |你的解答已提交 ✓
                    |
                    |AI 正在分析你的解题步骤…
                    |请稍候查看完整诊断报告。
                """.trimMargin()
            } else {
                Toast.makeText(requireContext(), "诊断报告功能开发中…", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<TextView>(R.id.erase_btn).setOnClickListener {
            canvas.clearCanvas()
        }
    }

    private fun addStepInput(container: LinearLayout) {
        stepCount++
        val stepLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = 8.dpToPx }
        }

        val label = TextView(requireContext()).apply {
            text = "步骤 $stepCount"
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
        }

        stepLayout.addView(label)
        stepLayout.addView(input)
        container.addView(stepLayout)
    }

    private val Int.dpToPx: Int
        get() = (this * resources.displayMetrics.density).toInt()
}
