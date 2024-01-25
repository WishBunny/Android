package com.wish.bunny.friend

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.wish.bunny.R
import org.w3c.dom.Text

/**
작성자:  이혜연
처리 내용: 초대 코드 발급 구현
 */
class InviteFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_invite, container, false)
        val copyText: TextView = view.findViewById(R.id.tv_invite_code)
        val copyBtn: Button = view.findViewById(R.id.btn_invite_copy)

        copyBtn.setOnClickListener{
            val text: String = copyText.text.toString()
            createClipData(text)
            true
        }
        return view
    }

    private fun createClipData(message: String){
        val clipboardManager: ClipboardManager = requireActivity()
            .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData: ClipData = ClipData.newPlainText("message", message)

        clipboardManager.setPrimaryClip(clipData)

        Toast.makeText(requireContext(), "복사되었습니다.", Toast.LENGTH_SHORT).show()

    }

}