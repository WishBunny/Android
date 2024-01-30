package com.wish.bunny.onboarding.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.wish.bunny.GlobalApplication
import com.wish.bunny.MainActivity
import com.wish.bunny.databinding.FragmentOnboardingLoginBinding
import com.wish.bunny.login.LoginService
import com.wish.bunny.login.domain.AccessToken
import com.wish.bunny.login.domain.MemberModel
import com.wish.bunny.util.RetrofitConnection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
    작성자: 엄상은
    처리 내용: 온보딩 마지막 페이지 로그인 처리
*/
class OnboardingLoginFragment : Fragment() {

    private var _binding: FragmentOnboardingLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnboardingLoginBinding.inflate(inflater, container, false)
        binding.kakaoLogin.setOnClickListener {
            Log.d("Login Activity Fragment", "카카오 로그인 버튼 클릭")
            kakaoLogin()
        }
        return binding.root
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
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireActivity())) {
            UserApiClient.instance.loginWithKakaoTalk(requireActivity()) { token, error ->
                if (error != null) {
                    Log.e("Login Activity", "카카오톡으로 로그인 실패", error)
                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(requireActivity(), callback = callback)
                } else if (token != null) {
                    Log.i("Login Activity", "카카오톡으로 로그인 성공 ${token.accessToken}")
                    postLoginAction(retrofitAPI, AccessToken(accessToken = token.accessToken))
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(requireActivity(), callback = callback)
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
                    Log.d("Login Activity", "계정 정보 불러오기 성공")
                    response.body()?.let { putAccessTokenToSharedPreferences(it) }
                } else {
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
        val intent = Intent(getActivity(), MainActivity::class.java)

        startActivity(intent)
        Log.d("Login Activity", "access token: " + it.toString())
        GlobalApplication.prefs.setString("accessToken", it.accessToken);
        // 확인용 로그
        Log.d("Login Activity", "shared preference에 저장된 access token: "
                + GlobalApplication.prefs.getString("accessToken", ""))
    }
}