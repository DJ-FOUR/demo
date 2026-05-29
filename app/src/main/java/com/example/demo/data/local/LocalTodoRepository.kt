package com.example.demo.data.local

import com.example.demo.model.TodoItem
import com.example.demo.repository.TodoRepository

class LocalTodoRepository : TodoRepository {

    private val todos = mutableListOf(
        TodoItem(1, "完成线性代数第三章习题"),
        TodoItem(2, "复习特征值与特征向量"),
        TodoItem(3, "英语虚拟语气专项练习", isCompleted = true)
    )
    private var nextId = 4L

    override fun getTodos(): List<TodoItem> = todos.toList()

    override fun addTodo(text: String): TodoItem {
        val item = TodoItem(id = nextId++, text = text)
        todos.add(item)
        return item
    }

    override fun toggleTodo(id: Long) {
        val idx = todos.indexOfFirst { it.id == id }
        if (idx >= 0) {
            todos[idx] = todos[idx].copy(isCompleted = !todos[idx].isCompleted)
        }
    }

    override fun deleteTodo(id: Long) {
        todos.removeAll { it.id == id }
    }

    override fun getCompletionPercent(): Float {
        if (todos.isEmpty()) return 0f
        return todos.count { it.isCompleted }.toFloat() / todos.size * 100f
    }
}
