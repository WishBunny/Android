package com.wish.bunny

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.wish.bunny.BuildConfig.NATIVE_APP_KEY
import com.wish.bunny.util.PreferenceUtil

/**
    작성자: 엄상은
    처리 내용: 카카오 로그인 SDK, Preferences 객체 초기화
*/
class GlobalApplication : Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate() {
        super.onCreate()
        // Shared Preference 사용을 위한 초기화
        prefs = PreferenceUtil(applicationContext)
        // Kakao SDK 초기화
        KakaoSdk.init(this, "${NATIVE_APP_KEY}")
    }
}