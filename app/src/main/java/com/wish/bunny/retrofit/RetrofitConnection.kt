package com.wish.bunny.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
    작성자: 엄상은
    처리 내용: 레트로핏 연결 설정 (싱글톤 객체)
*/
class RetrofitConnection {
    companion object {
        private const val BASE_URL = "http://10.0.2.2:8080/"
        private var INSTANCE: Retrofit? = null

        fun getInstance(): Retrofit {
            if (INSTANCE == null) {
                INSTANCE = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return INSTANCE!!
        }
    }
}