package com.example.demo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.R
import com.example.demo.model.ChatMessage

class ChatMessageAdapter(
    private var messages: List<ChatMessage>
) : RecyclerView.Adapter<ChatMessageAdapter.ViewHolder>() {

    fun updateMessages(newMessages: List<ChatMessage>) {
        messages = newMessages
        notifyDataSetChanged()
    }

    class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val content: TextView = view.findViewById(R.id.message_content)
        val timestamp: TextView? = view.findViewById(R.id.message_timestamp)
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isUser) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = if (viewType == 0) R.layout.item_message_user else R.layout.item_message_ai
        return ViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val msg = messages[position]
        holder.content.text = msg.content
        holder.timestamp?.text = msg.timestamp
    }

    override fun getItemCount() = messages.size
}
