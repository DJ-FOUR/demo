package com.example.demo.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import com.example.demo.model.TodoItem
import com.example.demo.repository.TodoRepository
import com.example.demo.repository.ProgressRepository
import java.util.Locale

class DashboardViewModel(
    private val todoRepository: TodoRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    var onDataChanged: (() -> Unit)? = null

    // Greeting
    val greeting: String
        get() {
            val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
            return when (hour) {
                in 0..11 -> "早安，学友"
                in 12..17 -> "午安，学友"
                else -> "晚安，学友"
            }
        }

    // Todos
    val todos: List<TodoItem> get() = todoRepository.getTodos()
    val masteryPercent: Float get() = todoRepository.getCompletionPercent()
    val weakPoints get() = progressRepository.getWeakPoints()

    fun addTodo(text: String) {
        todoRepository.addTodo(text)
        onDataChanged?.invoke()
    }

    fun toggleTodo(id: Long) {
        todoRepository.toggleTodo(id)
        onDataChanged?.invoke()
    }

    fun deleteTodo(id: Long) {
        todoRepository.deleteTodo(id)
        onDataChanged?.invoke()
    }

    // Timer
    var timerSeconds = 1480
        private set
    var isTimerRunning = false
        private set
    private val handler = Handler(Looper.getMainLooper())
    private var timerRunnable: Runnable? = null

    fun timerFormatted(): String {
        val min = timerSeconds / 60
        val sec = timerSeconds % 60
        return String.format(Locale.getDefault(), "%02d:%02d", min, sec)
    }

    fun toggleTimer(): Boolean {
        isTimerRunning = !isTimerRunning
        if (isTimerRunning) {
            val runnable = object : Runnable {
                override fun run() {
                    timerSeconds++
                    onDataChanged?.invoke()
                    handler.postDelayed(this, 1000)
                }
            }
            timerRunnable = runnable
            handler.post(runnable)
        } else {
            timerRunnable?.let { handler.removeCallbacks(it) }
        }
        onDataChanged?.invoke()
        return isTimerRunning
    }

    override fun onCleared() {
        super.onCleared()
        timerRunnable?.let { handler.removeCallbacks(it) }
    }
}
