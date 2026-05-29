package com.example.demo.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.AppViewModelFactory
import com.example.demo.R
import com.example.demo.adapter.ChatMessageAdapter
import com.example.demo.viewmodel.ChatViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChatBottomSheet : BottomSheetDialogFragment() {

    private lateinit var viewModel: ChatViewModel
    private lateinit var adapter: ChatMessageAdapter
    private lateinit var recycler: RecyclerView

    override fun getTheme(): Int = R.style.Theme_Demo_BottomSheet

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.dialog_chat, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, AppViewModelFactory())[ChatViewModel::class.java]

        recycler = view.findViewById(R.id.chat_recycler)
        val input = view.findViewById<EditText>(R.id.chat_input)
        val sendBtn = view.findViewById<ImageView>(R.id.chat_send)

        adapter = ChatMessageAdapter(viewModel.messages)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        sendBtn.setOnClickListener {
            val text = input.text.toString().trim()
            if (text.isNotEmpty()) {
                viewModel.sendMessage(text)
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

        viewModel.onDataChanged = {
            adapter.updateMessages(viewModel.messages)
        }
        viewModel.onScrollToEnd = {
            val count = viewModel.messages.size
            if (count > 0) {
                recycler.scrollToPosition(count - 1)
            }
        }
    }
}
