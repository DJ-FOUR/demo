package com.example.demo.ui.dashboard

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.R
import com.example.demo.adapter.TodoAdapter
import com.example.demo.model.TodoItem
import com.example.demo.view.MasteryGaugeView
import java.util.Locale

class DashboardFragment : Fragment() {

    private val todos = mutableListOf(
        TodoItem(1, "完成线性代数第三章习题"),
        TodoItem(2, "复习特征值与特征向量"),
        TodoItem(3, "英语虚拟语气专项练习", isCompleted = true)
    )

    private var isTimerRunning = false
    private var timerSeconds = 1480 // 24:38
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var timerRunnable: Runnable
    private lateinit var timerText: TextView
    private lateinit var timerToggle: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_dashboard, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Greeting
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        val greeting = view.findViewById<TextView>(R.id.greeting)
        greeting.text = when (hour) {
            in 0..11 -> getString(R.string.greeting_morning)
            in 12..17 -> getString(R.string.greeting_afternoon)
            else -> getString(R.string.greeting_evening)
        }

        // Mastery gauge
        val gauge = view.findViewById<MasteryGaugeView>(R.id.mastery_gauge)
        updateGaugeProgress(gauge)

        // Todo list
        val todoRecycler = view.findViewById<RecyclerView>(R.id.todo_recycler)
        val todoInput = view.findViewById<EditText>(R.id.todo_input)
        val todoAdd = view.findViewById<ImageView>(R.id.todo_add)

        lateinit var todoAdapter: TodoAdapter
        todoAdapter = TodoAdapter(
            onToggle = { item ->
                val idx = todos.indexOfFirst { it.id == item.id }
                if (idx >= 0) {
                    todos[idx] = item.copy(isCompleted = !item.isCompleted)
                    todoAdapter.submitList(todos.toList())
                    updateGaugeProgress(gauge)
                }
            },
            onDelete = { item ->
                todos.removeAll { it.id == item.id }
                todoAdapter.submitList(todos.toList())
                updateGaugeProgress(gauge)
            }
        )

        todoRecycler.layoutManager = LinearLayoutManager(requireContext())
        todoRecycler.adapter = todoAdapter
        todoAdapter.submitList(todos.toList())

        todoAdd.setOnClickListener {
            val text = todoInput.text.toString().trim()
            if (text.isNotEmpty()) {
                todos.add(TodoItem(text = text))
                todoAdapter.submitList(todos.toList())
                todoInput.text.clear()
                updateGaugeProgress(gauge)
            }
        }

        // Timer
        timerText = view.findViewById(R.id.timer_text)
        timerToggle = view.findViewById(R.id.timer_toggle)

        timerRunnable = object : Runnable {
            override fun run() {
                timerSeconds++
                val min = timerSeconds / 60
                val sec = timerSeconds % 60
                timerText.text = String.format(Locale.getDefault(), "%02d:%02d", min, sec)
                handler.postDelayed(this, 1000)
            }
        }

        timerToggle.setOnClickListener {
            if (isTimerRunning) {
                handler.removeCallbacks(timerRunnable)
                timerToggle.setImageResource(R.drawable.ic_play)
            } else {
                handler.post(timerRunnable)
                timerToggle.setImageResource(R.drawable.ic_pause)
            }
            isTimerRunning = !isTimerRunning
        }

        // Enter practice button
        view.findViewById<TextView>(R.id.enter_practice_btn).setOnClickListener {
            // Switch to course tab via activity
            activity?.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_nav)
                ?.selectedItemId = R.id.nav_course
        }
    }

    private fun updateGaugeProgress(gauge: MasteryGaugeView) {
        val completed = todos.count { it.isCompleted }
        val total = todos.size
        gauge.progress = if (total > 0) completed.toFloat() / total * 100f else 0f
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(timerRunnable)
    }
}
