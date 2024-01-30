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
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_confirm, null)

        builder.setView(view)

        val titleTextView: TextView = view.findViewById(R.id.dialog_title_tv)
        val descTextView: TextView = view.findViewById(R.id.dialog_desc_tv)
        val noButton: AppCompatButton = view.findViewById(R.id.dialog_no_btn)
        val yesButton: AppCompatButton = view.findViewById(R.id.dialog_yes_btn)

        descTextView.text = message
        titleTextView.text = title

        noButton.setOnClickListener {
            dismiss()
        }

        yesButton.setOnClickListener {
            callback?.invoke()
            dismiss()
        }

        return builder.create()
    }
}