package com.wish.bunny.wish

import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.wish.bunny.GlobalApplication
import com.wish.bunny.R
import com.wish.bunny.home.HomeFragment
import com.wish.bunny.util.RetrofitConnection
import com.wish.bunny.wish.domain.Message
import com.wish.bunny.wish.domain.WishItem
import com.wish.bunny.wish.domain.WishVo2
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WishUpdateFragment : Fragment() {

    private val accessToken = GlobalApplication.prefs.getString("accessToken", "")
//    private val accessToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2ZWE3NTFmOS1lNDhlLTQ1OWEtYjYwYi02MzFkMDM4ZmUwZmIiLCJpYXQiOjE3MDYyMjgzMDMsImV4cCI6MTcwODgyMDMwM30.x7mvX8xzWhd-lzB0xooHYIH9pSJfmsgzB7fe7tJhoUI"
    private lateinit var btnOpenCalendar: ImageButton
    private lateinit var tvSelectedDate: TextView
    private lateinit var backButton: ImageButton
    private lateinit var text_content: EditText
    private var selectedButton: Button? = null
    private lateinit var hashtagNo: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wish_update, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 클라이언트로부터 데이터 값 가져오기
        val wishNo = arguments?.getString("wishNo")
//        loadWishDetail("8dc58332-ee7f-4806-b492-494e4955bb33")

        if (wishNo != null) {
            Log.d("WishUpdateFragment", "Received wishNo: $wishNo")
            // wishNo를 사용하여 상세 정보를 로드하는 함수를 호출합니다.
            loadWishDetail(wishNo)
        }

        // 카테고리 버튼 처리
        val button1: Button = view.findViewById(R.id.to_do)
        val button2: Button = view.findViewById(R.id.to_eat)
        val button3: Button = view.findViewById(R.id.to_get)
        val categoryButtons = arrayOf(button1, button2, button3)

        // 해시태그 버튼 처리
        val hashtagButton1: Button = view.findViewById(R.id.hashtagButton1)
        val hashtagButton2: Button = view.findViewById(R.id.hashtagButton2)
        val hashtagButton3: Button = view.findViewById(R.id.hashtagButton3)
        val hashtagButton4: Button = view.findViewById(R.id.hashtagButton4)
        val hashtagButton5: Button = view.findViewById(R.id.hashtagButton5)
        val hashtagButton6: Button = view.findViewById(R.id.hashtagButton6)
        val hashtagButton7: Button = view.findViewById(R.id.hashtagButton7)
        val hashtagButton8: Button = view.findViewById(R.id.hashtagButton8)

        var selectedHashtag = ""

        hashtagButton1.setOnClickListener {
            selectedHashtag = "1"
            handleButtonClick(hashtagButton1)
        }

        hashtagButton2.setOnClickListener {
            selectedHashtag = "2"
            handleButtonClick(hashtagButton2)
        }

        hashtagButton3.setOnClickListener {
            selectedHashtag = "3"
            handleButtonClick(hashtagButton3)
        }

        hashtagButton4.setOnClickListener {
            selectedHashtag = "4"
            handleButtonClick(hashtagButton4)
        }

        hashtagButton5.setOnClickListener {
            selectedHashtag = "5"
            handleButtonClick(hashtagButton5)
        }

        hashtagButton6.setOnClickListener {
            selectedHashtag = "6"
            handleButtonClick(hashtagButton6)
        }

        hashtagButton7.setOnClickListener {
            selectedHashtag = "7"
            handleButtonClick(hashtagButton7)
        }

        hashtagButton8.setOnClickListener {
            selectedHashtag = "8"
            handleButtonClick(hashtagButton8)
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
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, HomeFragment()).commit()
        }

        // Update API 처리
        text_content = view.findViewById(R.id.content)
        tvSelectedDate = view.findViewById(R.id.tvSelectedDate)

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

        wishNo?.let { nonNullWishNo ->
            Log.d("WishUpdateFragment", "Received wishNo: $nonNullWishNo")
            loadWishDetail(nonNullWishNo)

            val btn_update: Button = view.findViewById(R.id.update)

            btn_update.setOnClickListener {
                val wvo2 = WishVo2(
                    category = selectedCategory,
                    content = text_content.text.toString(),
                    deadlineDt = tvSelectedDate.text.toString(),
                    notifyYn = "Y",
                    tagNo = selectedHashtag,
                    deleteTag = hashtagNo
                )

            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, HomeFragment()).commit()

                // Retrofit 인스턴스 생성 예시
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/")
                    .addConverterFactory(GsonConverterFactory.create()) // JSON 컨버터 사용
                    .client(OkHttpClient.Builder().addInterceptor { chain ->
                        val newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer $accessToken")
                            .build()
                        chain.proceed(newRequest)
                    }.build())
                    .build()

                // 위에서 만든 Retrofit 인스턴스를 사용하여 WishService의 구현체 생성
                val wishService = retrofit.create(WishService::class.java)

                val call: Call<Response<Message>> = wishService.wishUpdate(wvo2)

                call.enqueue(object : Callback<Response<Message>> {
                    override fun onResponse(call: Call<Response<Message>>, response: retrofit2.Response<Response<Message>>) {
                        if (response.isSuccessful) {
                            // 서버로부터 정상적인 응답을 받았을 때의 처리
                            Toast.makeText(requireContext(), "성공적으로 값을 전달했습니다", Toast.LENGTH_SHORT).show()
                            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
                            transaction.replace(R.id.fragment_container, HomeFragment()).commit()
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
                        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
                        transaction.replace(R.id.fragment_container, HomeFragment()).commit()
                    }
                })
            }
        }


    }

    // UI 업데이트 함수
    private fun updateUI(wishItem: WishItem) {
        val text_content: EditText = view?.findViewById(R.id.content) ?: return
        val tvSelectedDate: TextView = view?.findViewById(R.id.tvSelectedDate) ?: return
        val button1: Button = view?.findViewById(R.id.to_do) ?: return
        val button2: Button = view?.findViewById(R.id.to_eat) ?: return
        val button3: Button = view?.findViewById(R.id.to_get) ?: return
        val categoryButtons = arrayOf(button1, button2, button3)

        // 서버로부터 받은 데이터로 UI 업데이트
        text_content.setText(wishItem.content)
        tvSelectedDate.text = wishItem.deadlineDt

        hashtagNo = wishItem.tagNo
        val hashtagButtons : Button? = when (hashtagNo) {
            "1" -> view?.findViewById(R.id.hashtagButton1)
            "2" -> view?.findViewById(R.id.hashtagButton2)
            "3" -> view?.findViewById(R.id.hashtagButton3)
            "4" -> view?.findViewById(R.id.hashtagButton4)
            "5" -> view?.findViewById(R.id.hashtagButton5)
            "6" -> view?.findViewById(R.id.hashtagButton6)
            "7" -> view?.findViewById(R.id.hashtagButton7)
            "8" -> view?.findViewById(R.id.hashtagButton8)
            else -> null
        }

        // 찾은 버튼이 있으면 선택 상태로 설정합니다.
        hashtagButtons?.isSelected = true

        // 서버로부터 받은 tagNo에 해당하는 버튼을 활성화 시킴
            // 버튼의 UI 상태를 업데이트합니다.
            val pinkColor = ContextCompat.getColor(requireContext(), R.color.pink)
            val changeTextColor = ContextCompat.getColor(requireContext(), R.color.white)
            val transparentColor = ContextCompat.getColor(requireContext(), R.color.ivory)
            val originalTextColor = ContextCompat.getColor(requireContext(), R.color.black)

            hashtagButtons?.let {button ->
                if (button.isSelected) {
                    button.setBackgroundColor(pinkColor)
                    button.setTextColor(changeTextColor)
                } else {
                    // 비선택 상태의 배경
                    button.setBackgroundColor(transparentColor)
                    button.setTextColor(originalTextColor)
                }
        }

        // 모든 카테고리 버튼의 선택 상태를 초기화
        categoryButtons.forEach { it.isSelected = false }

        // 서버로부터 받은 카테고리에 해당하는 버튼을 선택 상태로 설정
        when (wishItem.category) {
            "do" -> button1.isSelected = true
            "eat" -> button2.isSelected = true
            "get" -> button3.isSelected = true
        }

        // 선택된 버튼의 UI를 갱신하는 코드가 필요합니다. 예를 들어:
        categoryButtons.forEach { button ->
            val pinkColor = ContextCompat.getColor(requireContext(), R.color.pink)
            val changeTextColor = ContextCompat.getColor(requireContext(), R.color.white)
            val transparentColor = ContextCompat.getColor(requireContext(), R.color.ivory)
            val originalTextColor = ContextCompat.getColor(requireContext(), R.color.black)

            if (button.isSelected) {
                button.setBackgroundColor(pinkColor)
                button.setTextColor(changeTextColor)
            } else {
                // 비선택 상태의 배경
                button.setBackgroundColor(transparentColor)
                button.setTextColor(originalTextColor)
            }
        }
    }

    private fun loadWishDetail(wishNo: String) {
        val retrofitAPI = RetrofitConnection.getInstance().create(WishService::class.java)
        retrofitAPI.getWishDetail(wishNo, "$accessToken").enqueue(object : Callback<WishItem> {
            override fun onResponse(call: Call<WishItem>, response: Response<WishItem>) {
                val wishItem = response.body()

                if (wishItem != null) {
                    updateUI(wishItem)
                    Log.d("wishItem 수정", wishItem.toString())

                } else {
                    Log.d("wishItem 수정", "서버 응답이 null입니다.")
                }
            }

            override fun onFailure(call: Call<WishItem>, t: Throwable) {
                Log.d("wishItem", "불러오기 실패: ${t.message}")
                // TODO: 실패 시 처리 구현
            }
        })
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