package com.example.demo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.R
import com.example.demo.model.TodoItem

class TodoAdapter(
    private val onToggle: (TodoItem) -> Unit,
    private val onDelete: (TodoItem) -> Unit
) : ListAdapter<TodoItem, TodoAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkbox: CheckBox = view.findViewById(R.id.todo_checkbox)
        val text: TextView = view.findViewById(R.id.todo_text)
        val deleteBtn: ImageView = view.findViewById(R.id.todo_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.checkbox.isChecked = item.isCompleted
        holder.text.text = item.text
        holder.text.alpha = if (item.isCompleted) 0.5f else 1f
        holder.checkbox.setOnClickListener { onToggle(item) }
        holder.deleteBtn.setOnClickListener { onDelete(item) }
    }

    class DiffCallback : DiffUtil.ItemCallback<TodoItem>() {
        override fun areItemsTheSame(old: TodoItem, new: TodoItem) = old.id == new.id
        override fun areContentsTheSame(old: TodoItem, new: TodoItem) = old == new
    }
}
