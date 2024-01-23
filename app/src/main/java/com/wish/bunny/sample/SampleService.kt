package com.wish.bunny.sample

import com.wish.bunny.sample.domain.Sample
import retrofit2.Call
import retrofit2.http.GET

/**
    작성자: 엄상은
    처리 내용: 레트로핏 테스트용 sample 서비스
*/
interface SampleService {
    @GET("sample/test")
    fun getSampleList(): Call<Sample>
}