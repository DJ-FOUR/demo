package com.example.demo.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.AppViewModelFactory
import com.example.demo.R
import com.example.demo.adapter.TodoAdapter
import com.example.demo.view.MasteryGaugeView
import com.example.demo.viewmodel.DashboardViewModel

class DashboardFragment : Fragment() {

    private lateinit var viewModel: DashboardViewModel
    private lateinit var todoAdapter: TodoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_dashboard, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, AppViewModelFactory())[DashboardViewModel::class.java]

        // Greeting
        view.findViewById<TextView>(R.id.greeting).text = viewModel.greeting

        // Mastery gauge
        val gauge = view.findViewById<MasteryGaugeView>(R.id.mastery_gauge)
        gauge.progress = viewModel.masteryPercent

        // Todo list
        val todoRecycler = view.findViewById<RecyclerView>(R.id.todo_recycler)
        val todoInput = view.findViewById<EditText>(R.id.todo_input)
        val todoAdd = view.findViewById<ImageView>(R.id.todo_add)

        todoAdapter = TodoAdapter(
            onToggle = { viewModel.toggleTodo(it.id) },
            onDelete = { viewModel.deleteTodo(it.id) }
        )

        todoRecycler.layoutManager = LinearLayoutManager(requireContext())
        todoRecycler.adapter = todoAdapter
        todoAdapter.submitList(viewModel.todos)

        todoAdd.setOnClickListener {
            val text = todoInput.text.toString().trim()
            if (text.isNotEmpty()) {
                viewModel.addTodo(text)
                todoInput.text.clear()
            }
        }

        // Timer
        val timerText = view.findViewById<TextView>(R.id.timer_text)
        val timerToggle = view.findViewById<ImageView>(R.id.timer_toggle)
        timerText.text = viewModel.timerFormatted()

        timerToggle.setOnClickListener {
            val running = viewModel.toggleTimer()
            timerToggle.setImageResource(
                if (running) R.drawable.ic_pause else R.drawable.ic_play
            )
        }

        // Enter practice button
        view.findViewById<TextView>(R.id.enter_practice_btn).setOnClickListener {
            activity?.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_nav)
                ?.selectedItemId = R.id.nav_course
        }

        // Observe data changes
        viewModel.onDataChanged = {
            todoAdapter.submitList(viewModel.todos)
            timerText.text = viewModel.timerFormatted()
            if (!viewModel.isTimerRunning) {
                timerToggle.setImageResource(R.drawable.ic_play)
            }
            gauge.progress = viewModel.masteryPercent
        }
    }
}
