package com.wish.bunny.emoji

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.GridLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.wish.bunny.R
import com.wish.bunny.emoji.domain.EmojiList
import com.wish.bunny.util.RetrofitConnection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
    작성자: 엄상은
    처리 내용: 커스텀 이모지 다이얼로그
*/
class EmojiDialog(private val context: Context) {
    private lateinit var dialog: AlertDialog
    private var emojis: List<String>? = null
    private val listeners = mutableListOf<() -> Unit>()

    init {
        val retrofitAPI = RetrofitConnection.getInstance().create(EmojiService::class.java)
        retrofitAPI.getEmojis().enqueue(object: Callback<EmojiList> {
            override fun onResponse(call: Call<EmojiList>, response: Response<EmojiList>) {
                if (response.isSuccessful) {
                    emojis = response.body()?.emoji
                    notifyEmojisLoaded()
                }
            }

            override fun onFailure(call: Call<EmojiList>, t: Throwable) {
                Log.d("EmojiDialog", "Failed to load emojis")
            }
        })
    }

    private fun notifyEmojisLoaded() {
        listeners.forEach { it.invoke() }
    }

    fun onEmojisLoaded(listener: () -> Unit) {
        if (emojis != null) {
            listener.invoke()
        } else {
            listeners.add(listener)
        }
    }

    fun show(onEmojiClick: (String) -> Unit) {
        onEmojisLoaded {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_emoji, null)
            val layout = dialogView.findViewById<GridLayout>(R.id.buttonContainer)
            for (emoji in emojis!!) {
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
}