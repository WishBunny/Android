package com.wish.bunny.util

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.GridLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.wish.bunny.R

class EmojiDialog(private val context: Context, private val emojis: Array<String>) {
    private lateinit var dialog: AlertDialog

    fun show(onEmojiClick: (String) -> Unit) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_emoji, null)
        val layout = dialogView.findViewById<GridLayout>(R.id.buttonContainer)
        for (emoji in emojis) {
            val button = Button(context)
            button.text = emoji
            button.textSize = 30f
            button.typeface = ResourcesCompat.getFont(context, R.font.tossface_font)
            button.background = ContextCompat.getDrawable(context, android.R.color.transparent)

            button.setOnClickListener {
                onEmojiClick(emoji)
                dialog.dismiss()
            }

            val layoutParams = GridLayout.LayoutParams()
            layoutParams.width = 0
            layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT
            layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            button.layoutParams = layoutParams

            layout.addView(button)
        }

        val builder = AlertDialog.Builder(context)
        builder.setView(dialogView)
        dialog = builder.create()

        dialog.show()
    }

}
