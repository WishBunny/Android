package com.wish.bunny.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.wish.bunny.GlobalApplication
import com.wish.bunny.MainActivity
import com.wish.bunny.R
import com.wish.bunny.login.domain.AccessToken
import com.wish.bunny.login.domain.MemberModel
import com.wish.bunny.util.RetrofitConnection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
    작성자: 엄상은
    처리 내용: 카카오 로그인 액티비티
*/
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val kakaoLoginButton = findViewById<ImageView>(R.id.kakao_login)
        kakaoLoginButton.setOnClickListener {
            kakaoLogin()
        }
    }

    /**
        작성자: 엄상은
        처리 내용: 카카오 로그인 처리 (카카오 SDK 이용)
    */
    private fun kakaoLogin() {
        // 통신을 위한 레트로핏 인스턴스 선언
        val retrofitAPI = RetrofitConnection.getInstance().create(LoginService::class.java)
        Log.i("Login Activity", "카카오계정으로 로그인 시도")
        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("Login Activity", "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i("Login Activity", "카카오계정으로 로그인 성공 ${token.accessToken}")
                postLoginAction(retrofitAPI, AccessToken(accessToken = token.accessToken))
            }
            else {
                Log.e("Login Activity", "로그인 시도 실패")
            }
        }
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e("Login Activity", "카카오톡으로 로그인 실패", error)
                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    Log.i("Login Activity", "카카오톡으로 로그인 성공 ${token.accessToken}")
                    postLoginAction(retrofitAPI, AccessToken(accessToken = token.accessToken))
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

    /**
        작성자: 엄상은
        처리 내용: 서버로 로그인 요청 보내기
    */
    private fun postLoginAction(retrofitAPI: LoginService, accessToken: AccessToken) {
        Log.d("Login Activity", accessToken.toString())
        retrofitAPI.loginByAccessToken(accessToken).enqueue(object:
            Callback<MemberModel> {
            override fun onResponse(
                call: Call<MemberModel>,
                response: Response<MemberModel>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "계정 정보 불러오기 성공!", Toast.LENGTH_SHORT)
                        .show()
                    Log.d("Login Activity", "계정 정보 불러오기 성공")
                    response.body()?.let { putAccessTokenToSharedPreferences(it) }
                } else {
                    Toast.makeText(this@LoginActivity, "계정 정보 불러오기 실패.", Toast.LENGTH_SHORT)
                        .show()
                    Log.d("Login Activity", "계정 정보 불러오기 실패")
                }
            }
            override fun onFailure(call: Call<MemberModel>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    /**
        작성자: 엄상은
        처리 내용: Shared Preferences에 JWT 토큰 저장
    */
    private fun putAccessTokenToSharedPreferences(it: MemberModel) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        Log.d("Login Activity", "access token: " + it.toString())
        findViewById<TextView>(R.id.textAccessToken).text = it.accessToken
        GlobalApplication.prefs.setString("accessToken", it.accessToken);
        // 확인용 로그
        Log.d("Login Activity", "shared preference에 저장된 access token: "
                + GlobalApplication.prefs.getString("accessToken", ""))
    }
}