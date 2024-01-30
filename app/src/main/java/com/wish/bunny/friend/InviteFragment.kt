package com.wish.bunny.friend

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.wish.bunny.GlobalApplication
import com.wish.bunny.R
import com.wish.bunny.friend.domain.FriendResponse
import com.wish.bunny.util.RetrofitConnection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
작성자:  이혜연
처리 내용: 초대 코드 발급 및 클립보드 복사 구현
 */
class InviteFragment : Fragment() {
    private val inviteCodeLiveData = MutableLiveData<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_invite, container, false)
        val copyText: TextView = view.findViewById(R.id.tv_invite_code)
        val copyBtn: Button = view.findViewById(R.id.btn_invite_copy)

        val retrofitAPI = RetrofitConnection.getInstance().create(FriendService::class.java)
        createInviteCode(retrofitAPI)
        inviteCodeLiveData.observe(viewLifecycleOwner, Observer { inviteCode ->
            copyText.text = inviteCode
        })

        copyBtn.setOnClickListener {
            val text: String = copyText.text.toString()
            createClipData(text)
            true
        }

        val backImageView: ImageView = view.findViewById(R.id.iv_back_invite)

        backImageView.setOnClickListener {
            navigateToPreviousFragment()
        }
        return view
    }

    private fun createClipData(message: String) {
        val clipboardManager: ClipboardManager = requireActivity()
            .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData: ClipData = ClipData.newPlainText("message", message)

        clipboardManager.setPrimaryClip(clipData)

        Toast.makeText(requireContext(), "복사되었습니다.", Toast.LENGTH_SHORT).show()

    }

    fun createInviteCode(retrofitAPI: FriendService) {
        var accessToken = GlobalApplication.prefs.getString("accessToken", "")
        accessToken?.let {
            retrofitAPI.createInviteCode(it).enqueue(object :
                Callback<FriendResponse> {
                override fun onResponse(
                    call: Call<FriendResponse>,
                    response: Response<FriendResponse>
                ) {
                    if (response.isSuccessful) {
                        val inviteCode = response.body()!!.data.inviteCode
                        inviteCodeLiveData.postValue(inviteCode)
                    } else {
                        Log.d("createInviteCode", "초대 코드 발급 실패")
                    }
                }

                override fun onFailure(call: Call<FriendResponse>, t: Throwable) {
                    t.printStackTrace()
                }

            })

        }
    }

    private fun navigateToPreviousFragment() {
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        fragmentManager.popBackStack()
    }
}