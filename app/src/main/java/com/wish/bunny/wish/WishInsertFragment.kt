package com.wish.bunny.wish

import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import com.wish.bunny.R
import java.text.SimpleDateFormat
import java.util.*

class WishInsertFragment : Fragment() {
    private lateinit var btnOpenCalendar: ImageButton
    private lateinit var tvSelectedDate: TextView
    private var selectedButton: Button? = null
    private lateinit var backButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wish_insert, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 카테고리 버튼 처리
        val button1: Button = view.findViewById(R.id.button1)
        val button2: Button = view.findViewById(R.id.button2)
        val button3: Button = view.findViewById(R.id.button3)

        val pinkColor = ContextCompat.getColor(requireContext(), R.color.pink)
        val changeTextColor = ContextCompat.getColor(requireContext(), R.color.white)
        val transparentColor = ContextCompat.getColor(requireContext(), R.color.ivory)
        val originalTextColor = ContextCompat.getColor(requireContext(), R.color.black)

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

        // 해시태그 버튼 처리
        val buttons = arrayOf(
            view.findViewById<Button>(R.id.hashtagButton1),
            view.findViewById<Button>(R.id.hashtagButton2),
            view.findViewById<Button>(R.id.hashtagButton3),
            view.findViewById<Button>(R.id.hashtagButton4),
            view.findViewById<Button>(R.id.hashtagButton5),
            view.findViewById<Button>(R.id.hashtagButton6),
            view.findViewById<Button>(R.id.hashtagButton7),
            view.findViewById<Button>(R.id.hashtagButton8),
        )

        buttons.forEach { button ->
            button.setOnClickListener { handleButtonClick(it as Button) }
        }

        // 달력 버튼 처리
        btnOpenCalendar = view.findViewById(R.id.btnOpenCalendar)
        tvSelectedDate = view.findViewById(R.id.tvSelectedDate)

        view.findViewById<ImageButton>(R.id.btnOpenCalendar).setOnClickListener {
            showDatePicker()
        }

        view.findViewById<ImageButton>(R.id.reset).setOnClickListener {
            resetDate()
        }

        // 뒤로가기 버튼 초기화
        backButton = view.findViewById(R.id.back)

        // 뒤로가기 버튼에 클릭 리스너 추가
        backButton.setOnClickListener {
            // WishList 클래스로 이동
            val intent = Intent(requireContext(), WishList::class.java)
            startActivity(intent)
        }
    }

    // 달력 이벤트 핸들러
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)
            val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 EEEE까지", Locale.getDefault())
            tvSelectedDate.text = dateFormat.format(selectedDate.time)
        }, year, month, day).show()
    }

    private fun resetDate() {
        tvSelectedDate.text = getString(R.string.select_date)
    }

    // 해시태그 버튼 클릭 이벤트 처리
    // 버튼 클릭 이벤트를 처리하는 메소드
    private fun handleButtonClick(clickedButton: Button) {
        // 이전에 선택된 버튼의 상태를 복원합니다.
        selectedButton?.let {
            it.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.ivory)
            )
            it.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }

        // 새로 클릭된 버튼을 선택 상태로 만듭니다.
        clickedButton.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), R.color.pink)
        )
        clickedButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

        // 선택된 버튼을 업데이트합니다.
        selectedButton = clickedButton
    }

    // 버튼의 선택 여부에 따라 상태 업데이트하는 함수
    private fun updateButtonState(button: Button, isSelected: Boolean) {
        val pinkColor = ContextCompat.getColor(requireContext(), R.color.pink)
        val transparentColor = ContextCompat.getColor(requireContext(), R.color.ivory)

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