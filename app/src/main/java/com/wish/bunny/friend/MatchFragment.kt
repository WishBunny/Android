package com.wish.bunny.friend


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.wish.bunny.GlobalApplication
import com.wish.bunny.R
import com.wish.bunny.friend.domain.MatchingRequest
import com.wish.bunny.friend.domain.MatchingResponse
import com.wish.bunny.util.RetrofitConnection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
작성자:  이혜연
처리 내용: 친구 매칭하기 구현
 */
class MatchFragment : Fragment() {

    private val matchingLiveData = MutableLiveData<Int>()
    private val SUCCESS_DIALOG_LAYOUT = com.wish.bunny.R.layout.dialog_matching_success
    private val FAILURE_DIALOG_LAYOUT = com.wish.bunny.R.layout.dialog_matching_fail

    private lateinit var dialogBuilder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(com.wish.bunny.R.layout.fragment_match, container, false)
        val inviteCode: TextView = view.findViewById(com.wish.bunny.R.id.et_match)
        val matchingBtn: Button = view.findViewById(com.wish.bunny.R.id.btn_match_end)

        val retrofitAPI = RetrofitConnection.getInstance().create(FriendService::class.java)


        matchingLiveData.observe(viewLifecycleOwner, Observer { result ->
            if (result != 0) {
                showAlertDialog(SUCCESS_DIALOG_LAYOUT)
                navigateToPreviousFragment()
            } else {
                showAlertDialog(FAILURE_DIALOG_LAYOUT)
            }
        })

        matchingBtn.setOnClickListener {
            val request = MatchingRequest(inviteCode.text.toString())
            matchResult(retrofitAPI, request)
        }

        val backImageView: ImageView = view.findViewById(R.id.iv_back_match)

        backImageView.setOnClickListener {
            navigateToPreviousFragment()
        }
        return view
    }

    private fun matchResult(retrofit: FriendService, matchingRequest: MatchingRequest) {
        retrofit.matchFriend(GlobalApplication.prefs.getString("accessToken", ""), matchingRequest)
            .enqueue(object : Callback<MatchingResponse> {
                override fun onResponse(
                    call: Call<MatchingResponse>, response: Response<MatchingResponse>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()?.data ?: 0
                        matchingLiveData.postValue(result)
                    } else {
                        matchingLiveData.postValue(0)
                    }
                }

                override fun onFailure(call: Call<MatchingResponse>, t: Throwable) {
                    t.printStackTrace()
                    matchingLiveData.postValue(0)
                }
            })
    }


    private fun showAlertDialog(layout: Int) {
        dialogBuilder = AlertDialog.Builder(requireContext())
        val layoutView = LayoutInflater.from(requireContext()).inflate(layout, null)
        val dialogButton = layoutView.findViewById<Button>(com.wish.bunny.R.id.btnDialog)

        dialogBuilder.setView(layoutView)
        alertDialog = dialogBuilder.create()
        alertDialog.show()

        dialogButton.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    private fun navigateToPreviousFragment() {
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        fragmentManager.popBackStack()
    }
}