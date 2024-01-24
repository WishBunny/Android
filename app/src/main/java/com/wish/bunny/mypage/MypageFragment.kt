package com.wish.bunny.mypage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wish.bunny.GlobalApplication
import com.wish.bunny.databinding.FragmentMypageBinding

class MypageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        binding.LogoutBtn.setOnClickListener {
            logout()
        }
        return binding.root
    }

    /**
        작성자: 엄상은
        처리 내용: 로그아웃 처리
     */
    private fun logout() {
        Log.d("MyPageFragement", "로그아웃 시도")
        GlobalApplication.prefs.removeKeyWithValue("accessToken")
        val accessToken = GlobalApplication.prefs.getString("accessToken", "")
        if (accessToken == "") {
            Log.d("MyPageFragement", "로그아웃 완료")
        } else {
            Log.d("MyPageFragement", "로그아웃 실패")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
