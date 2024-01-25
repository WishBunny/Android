package com.wish.bunny.wish

import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.wish.bunny.R
import com.wish.bunny.wish.domain.Message
import com.wish.bunny.wish.domain.WishVo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
/**
작성자: 황수연
처리 내용: 위시 등록 화면 및 API 구현
 */
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
        val button1: Button = view.findViewById(R.id.to_do)
        val button2: Button = view.findViewById(R.id.to_eat)
        val button3: Button = view.findViewById(R.id.to_get)
        val categoryButtons = arrayOf(button1, button2, button3)

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

        // Insert API 처리
        val text_content: EditText = view.findViewById(R.id.content)
        val tvSelectedDate: TextView = view.findViewById(R.id.tvSelectedDate)

        var selectedCategory = ""

        button1.setOnClickListener {
            selectedCategory = "do"
            updateCategoryButtons(button1, categoryButtons)
        }

        button2.setOnClickListener {
            selectedCategory = "eat"
            updateCategoryButtons(button2, categoryButtons)
        }

        button3.setOnClickListener {
            selectedCategory = "get"
            updateCategoryButtons(button3, categoryButtons)
        }

        val btn_insert: Button = view.findViewById(R.id.insert)

        btn_insert.setOnClickListener {
            // tagNo를 위한 StringBuilder 객체
            val tagNoBuilder = StringBuilder()

            // 선택된 버튼의 텍스트를 StringBuilder에 추가
            for (button in buttons) {
                if (button.isSelected) {
                    tagNoBuilder.append(button.text.toString()).append(" ")
                }
            }

            // StringBuilder에서 마지막 공백 제거
            val tagNo = if (tagNoBuilder.isNotEmpty()) {
                tagNoBuilder.substring(0, tagNoBuilder.length - 1)
            } else {
                tagNoBuilder.toString()
            }

            val wvo = WishVo(
                category = selectedCategory,
                content = text_content.text.toString(),
                deadlineDt = tvSelectedDate.text.toString(),
                notifyYn = "Y",
                writerNo = "123",
                achieveYn = "N",
                tagNo = tagNo
            )

            // Retrofit 인스턴스 생성 예시
            val retrofit = Retrofit.Builder()
                .baseUrl("http://172.18.160.1:8080/")
                .addConverterFactory(GsonConverterFactory.create()) // JSON 컨버터 사용
                .build()

            // 위에서 만든 Retrofit 인스턴스를 사용하여 WishService의 구현체 생성
            val wishService = retrofit.create(WishService::class.java)

            val call: Call<Response<Message>> = wishService.wishInsert(wvo)

            call.enqueue(object : Callback<Response<Message>> {
                override fun onResponse(call: Call<Response<Message>>, response: retrofit2.Response<Response<Message>>) {
                    if (response.isSuccessful) {
                        // 서버로부터 정상적인 응답을 받았을 때의 처리
                        val message = response.body()?.message()
                        Toast.makeText(
                            requireContext(),
                            message ?: "성공적으로 값을 전달했습니다",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // 서버로부터 정상적인 응답을 받지 못했을 때의 처리
                        Toast.makeText(
                            requireContext(),
                            "서버로부터 예상치 못한 응답을 받았습니다: ${response.errorBody()?.string()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Response<Message>>, t: Throwable) {
                    // 네트워크 요청 자체가 실패했을 때의 처리
                    t.printStackTrace()
                }
            })
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

    // 버튼 클릭 이벤트 처리
    private fun updateCategoryButtons(selectedButton: Button, allButtons: Array<Button>) {
        val pinkColor = ContextCompat.getColor(requireContext(), R.color.pink)
        val changeTextColor = ContextCompat.getColor(requireContext(), R.color.white)
        val transparentColor = ContextCompat.getColor(requireContext(), R.color.ivory)
        val originalTextColor = ContextCompat.getColor(requireContext(), R.color.black)

        for (button in allButtons) {
            if (button == selectedButton) {
                // 클릭된 버튼인 경우
                button.setBackgroundColor(pinkColor)
                button.setTextColor(changeTextColor)
            } else {
                // 클릭된 버튼이 아닌 경우
                button.setBackgroundColor(transparentColor)
                button.setTextColor(originalTextColor)
            }
        }
    }

}