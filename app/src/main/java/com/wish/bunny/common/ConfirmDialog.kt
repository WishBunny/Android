package com.wish.bunny.common

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.wish.bunny.R


/**
작성자:  이혜연
처리 내용: custom dialog 구현
 */
class ConfirmDialog(
    private val context: Context,
    private val title: String,
    private val message: String,
    private val callback: (() -> Unit)?,
    private val positiveButtonText: String,
    private val requestCode: Int
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)

        // 레이아웃 인플레이터를 가져옴
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_confirm, null)

        // 다이얼로그에 사용자 정의 레이아웃 설정
        builder.setView(view)

        // 사용자 정의 레이아웃에서 뷰를 찾음
        val titleTextView: TextView = view.findViewById(R.id.dialog_title_tv)
        val descTextView: TextView = view.findViewById(R.id.dialog_desc_tv)
        val noButton: AppCompatButton = view.findViewById(R.id.dialog_no_btn)
        val yesButton: AppCompatButton = view.findViewById(R.id.dialog_yes_btn)

        // 제목과 메시지를 뷰에 설정
        descTextView.text = message
        titleTextView.text = title
        // 필요에 따라 다른 속성이나 메시지 설정

        // 버튼 클릭 리스너 설정
        noButton.setOnClickListener {
            dismiss()
        }

        yesButton.setOnClickListener {
            // 콜백이 null이 아니면 호출
            callback?.invoke()
            dismiss()
        }

        return builder.create()
    }
}