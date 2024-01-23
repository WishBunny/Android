package com.wish.bunny.wish

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.wish.bunny.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WishInsertActivity : AppCompatActivity() {
    private val btnOpenCalendar: Button by lazy { findViewById<Button>(R.id.btnOpenCalendar) }
    private val tvSelectedDate: TextView by lazy { findViewById<TextView>(R.id.tvSelectedDate) }
    private val selectedDate: Calendar = Calendar.getInstance()
    private val selectedButtons: MutableList<Button> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert)

        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)

        val pinkColor = ContextCompat.getColor(this, R.color.pink)
        val changeTextColor = ContextCompat.getColor(this, R.color.white)
        val transparentColor = ContextCompat.getColor(this, R.color.ivory)
        val originalTextColor = ContextCompat.getColor(this, R.color.black) // 원래의 글자색 저장

        button1.setOnClickListener {
            button1.setBackgroundColor(pinkColor) // 핑크색으로 변경
            button1.setTextColor(changeTextColor) // 글자색을 원래대로
            button2.setBackgroundColor(transparentColor) // 다른 버튼은 원래 색으로
            button2.setTextColor(originalTextColor)
            button3.setBackgroundColor(transparentColor)
            button3.setTextColor(originalTextColor)
        }

        button2.setOnClickListener {
            button2.setBackgroundColor(pinkColor)
            button2.setTextColor(changeTextColor) // 글자색을 원래대로
            button1.setBackgroundColor(transparentColor) // 다른 버튼은 원래 색으로
            button1.setTextColor(originalTextColor)
            button3.setBackgroundColor(transparentColor)
            button3.setTextColor(originalTextColor)
        }

        button3.setOnClickListener {
            button3.setBackgroundColor(pinkColor)
            button3.setTextColor(changeTextColor) // 글자색을 원래대로
            button1.setBackgroundColor(transparentColor) // 다른 버튼은 원래 색으로
            button1.setTextColor(originalTextColor)
            button2.setBackgroundColor(transparentColor)
            button2.setTextColor(originalTextColor)
        }
    }

    // 클릭 이벤트 핸들러
    fun onCalendarButtonClick(view: View) {
        openDatePickerDialog()
    }

    private fun openDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            dateSetListener,
            selectedDate[Calendar.YEAR],
            selectedDate[Calendar.MONTH],
            selectedDate[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.show()
    }

    private val dateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            selectedDate[Calendar.YEAR] = year
            selectedDate[Calendar.MONTH] = month
            selectedDate[Calendar.DAY_OF_MONTH] = dayOfMonth
            updateSelectedDate()
        }

    private fun updateSelectedDate() {
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 EEEE까지", Locale.getDefault())
        val formattedDate = dateFormat.format(selectedDate.time)
        tvSelectedDate.text = formattedDate
    }

    // 해시태그 버튼 클릭 이벤트 처리
    fun onHashtagButtonClick(view: View) {
        val clickedButton = view as Button

        // 이미 선택된 버튼인지 확인
        if (selectedButtons.contains(clickedButton)) {
            // 이미 선택된 경우, 선택 해제
            selectedButtons.remove(clickedButton)
            updateButtonState(clickedButton, isSelected = false)
        } else {
            // 선택되지 않은 경우, 최대 선택 개수 확인 후 선택
            if (selectedButtons.size < 2) {
                selectedButtons.add(clickedButton)
                updateButtonState(clickedButton, isSelected = true)
            }
        }
    }

    // 버튼의 선택 여부에 따라 상태 업데이트하는 함수
    private fun updateButtonState(button: Button, isSelected: Boolean) {
        val pinkColor = ContextCompat.getColor(this, R.color.pink)
        val transparentColor = ContextCompat.getColor(this, R.color.ivory)

        button.isSelected = isSelected

        if (isSelected) {
            // 선택된 경우
            button.setTextColor(Color.WHITE)
            button.setBackgroundColor(pinkColor)
        } else {
            // 선택 해제된 경우
            button.setTextColor(Color.BLACK)
            button.setBackgroundColor(transparentColor)
        }
    }
}
