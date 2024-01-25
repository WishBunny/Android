package com.wish.bunny.wish

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.wish.bunny.R
import com.wish.bunny.util.RetrofitConnection
import com.wish.bunny.wish.domain.WishItem
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WishDetail : AppCompatActivity() {

    private val btnOpenCalendar: Button by lazy { findViewById<Button>(R.id.btnOpenCalendar) }
    private val tvSelectedDate: TextView by lazy { findViewById<TextView>(R.id.tvSelectedDate) }
    private val selectedDate: Calendar = Calendar.getInstance()
    private val selectedButtons: MutableList<Button> = mutableListOf()

    // 원래의 글자색 저장
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wish_detail)

        loadWishDetail("WISH002")


    }
    private fun loadWishDetail(wishNo: String) {
        val retrofitAPI = RetrofitConnection.getInstance().create(WishService::class.java)
        retrofitAPI.getWishDetail(wishNo).enqueue(object : retrofit2.Callback<WishItem> {
            override fun onResponse(call: Call<WishItem>, response: Response<WishItem>) {
                // 성공 시 처리
                val wishItem = response.body()

                if (wishItem != null) {
                    updateUI(wishItem)
                    Log.d("wishItem", wishItem.toString())

                } else {
                    Log.d("wishItem", "서버 응답이 null입니다.")
                }
            }

            override fun onFailure(call: Call<WishItem>, t: Throwable) {
                Log.d("wishItem", "불러오기 실패: ${t.message}")
                // TODO: 실패 시 처리 구현
            }
        })
    }

    private fun updateUI(wishItem: WishItem) {
        val content = findViewById<TextView>(R.id.content)
        val hashTag = findViewById<Button>(R.id.hashtagButton1)
        val deadline = findViewById<TextView>(R.id.tvSelectedDate)

        content.text = wishItem.content
        hashTag.text= '#'+wishItem.tagContents
        deadline.text = wishItem.deadlineDt
        EditDeleteButtonShowYn(wishItem.writerYn)
        setCategroy(wishItem.category)
    }
    private fun EditDeleteButtonShowYn(writerYn: String){
        val editBtn = findViewById<Button>(R.id.updateBtn)
        val deleteBtn = findViewById<Button>(R.id.deleteBtn)

        if(writerYn.equals("Y")){
            editBtn.visibility = View.VISIBLE
            deleteBtn.visibility = View.VISIBLE
        }else{
            editBtn.visibility = View.GONE
            deleteBtn.visibility = View.GONE
        }

    }
    private fun setCategroy(category : String){
        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)

        val pinkColor = ContextCompat.getColor(this, R.color.pink)
        val changeTextColor = ContextCompat.getColor(this, R.color.white)
        val transparentColor = ContextCompat.getColor(this, R.color.ivory)
        val originalTextColor = ContextCompat.getColor(this, R.color.black)

        if(category.equals("go")){
            button1.setBackgroundColor(pinkColor) // 핑크색으로 변경
            button1.setTextColor(changeTextColor) // 글자색을 원래대로
            button2.setBackgroundColor(transparentColor) // 다른 버튼은 원래 색으로
            button2.setTextColor(originalTextColor)
            button3.setBackgroundColor(transparentColor)
            button3.setTextColor(originalTextColor)
        }else  if(category.equals("eat")){
            button2.setBackgroundColor(pinkColor)
            button2.setTextColor(changeTextColor) // 글자색을 원래대로
            button1.setBackgroundColor(transparentColor) // 다른 버튼은 원래 색으로
            button1.setTextColor(originalTextColor)
            button3.setBackgroundColor(transparentColor)
            button3.setTextColor(originalTextColor)
        }else{
            button3.setBackgroundColor(pinkColor)
            button3.setTextColor(changeTextColor) // 글자색을 원래대로
            button1.setBackgroundColor(transparentColor) // 다른 버튼은 원래 색으로
            button1.setTextColor(originalTextColor)
            button2.setBackgroundColor(transparentColor)
            button2.setTextColor(originalTextColor)
        }
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