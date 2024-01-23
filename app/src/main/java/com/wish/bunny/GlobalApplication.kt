package com.wish.bunny

import android.app.Application
/*import com.kakao.sdk.common.KakaoSdk
import com.wish.bunny.BuildConfig.NATIVE_APP_KEY*/

/**
    작성자: 엄상은
    처리 내용: 카카오 로그인 SDK 초기화
*/
class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Kakao SDK 초기화
      //  KakaoSdk.init(this, "${NATIVE_APP_KEY}")
    }
}