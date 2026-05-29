package com.example.demo.repository

import com.example.demo.model.TodoItem

interface TodoRepository {
    fun getTodos(): List<TodoItem>
    fun addTodo(text: String): TodoItem
    fun toggleTodo(id: Long)
    fun deleteTodo(id: Long)
    fun getCompletionPercent(): Float
}
