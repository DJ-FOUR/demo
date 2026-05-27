package com.example.demo.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.R
import com.example.demo.adapter.ChatMessageAdapter
import com.example.demo.model.ChatMessage
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChatBottomSheet : BottomSheetDialogFragment() {

    private val messages = mutableListOf(
        ChatMessage(1, "你好！我是你的 AI 专属学术导师，有任何学习问题随时问我。", false, "10:30"),
        ChatMessage(2, "老师，特征值和特征向量的几何意义是什么？", true, "10:31"),
        ChatMessage(3, "特征向量表示线性变换中方向不变的特殊向量，而特征值表示该向量被拉伸或压缩的比例。几何上，如果把矩阵想象成空间的变形，特征向量就是变形后仍保持在同一直线上的方向。", false, "10:32")
    )

    override fun getTheme(): Int = R.style.Theme_Demo_BottomSheet

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.dialog_chat, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.chat_recycler)
        val input = view.findViewById<EditText>(R.id.chat_input)
        val sendBtn = view.findViewById<ImageView>(R.id.chat_send)

        val adapter = ChatMessageAdapter(messages)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        sendBtn.setOnClickListener {
            val text = input.text.toString().trim()
            if (text.isNotEmpty()) {
                messages.add(ChatMessage(content = text, isUser = true, timestamp = "刚刚"))
                adapter.notifyItemInserted(messages.size - 1)
                recycler.scrollToPosition(messages.size - 1)
                input.text.clear()
            }
        }

        // Quick action chips
        view.findViewById<TextView>(R.id.chip_1).setOnClickListener {
            input.setText(R.string.chat_quick_1)
        }
        view.findViewById<TextView>(R.id.chip_2).setOnClickListener {
            input.setText(R.string.chat_quick_2)
        }
        view.findViewById<TextView>(R.id.chip_3).setOnClickListener {
            input.setText(R.string.chat_quick_3)
        }
    }
}
