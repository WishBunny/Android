package com.wish.bunny.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.wish.bunny.R
import com.wish.bunny.util.RetrofitConnection
import com.wish.bunny.sample.domain.Sample
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
    작성자: 엄상은
    처리 내용: 레트로핏 테스트용 sample activity
*/
class SampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
        loadSample()
    }

    private fun loadSample() {
        val retrofitAPI = RetrofitConnection.getInstance().create(SampleService::class.java)
        retrofitAPI.getSampleList().enqueue(object: Callback<Sample> {
            override fun onResponse(call: Call<Sample>, response: Response<Sample>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@SampleActivity, "불러오기 성공!", Toast.LENGTH_SHORT).show()
                    Log.d("loadSample()", "불러오기 성공")
                    response.body()?.let { updateUI(it) }
                }
                else {
                    Toast.makeText(this@SampleActivity, "불러오기 실패.", Toast.LENGTH_SHORT).show()
                    Log.d("loadSample()", "불러오기 실패")
                }
            }

            override fun onFailure(call: Call<Sample>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun updateUI(sampleData: Sample) {
        val sampleItem = sampleData[0] // 리스트의 첫 번째 아이템을 가져옵니다. 원하는 아이템을 선택하세요.

        val nameTextView = findViewById<TextView>(R.id.nameTextView)
        val userIdTextView = findViewById<TextView>(R.id.userIdTextView)
        val userPwTextView = findViewById<TextView>(R.id.userPwTextView)

        // TextView에 정보를 표시합니다.
        nameTextView.text = sampleItem.name
        userIdTextView.text = sampleItem.userId
        userPwTextView.text = sampleItem.userPw
    }
}