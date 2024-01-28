package com.wish.bunny.friend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wish.bunny.GlobalApplication
import com.wish.bunny.R
import com.wish.bunny.alarm.AlarmFunctions
import com.wish.bunny.common.ConfirmDialog
import com.wish.bunny.databinding.FragmentFriendBinding
import com.wish.bunny.friend.domain.FriendDeleteResponse
import com.wish.bunny.friend.domain.FriendListResponse
import com.wish.bunny.friend.domain.Profiles
import com.wish.bunny.home.HomeFragment
import com.wish.bunny.util.RetrofitConnection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
작성자:  이혜연
처리 내용: 친구 리스트 조회, 삭제 아이콘 클릭시 삭제 구현
 */
class FriendFragment : Fragment() {
    lateinit var rc_friends: RecyclerView
    lateinit var adapter_profile: ProfileAdapter
    var profileList: MutableList<Profiles> = ArrayList() // 변경된 부분: List를 MutableList로 변경

    private var _binding: FragmentFriendBinding? = null
    private val binding get() = _binding!!

    private val friendListLiveData = MutableLiveData<List<Profiles>>()
    private val friendDeleteLiveData = MutableLiveData<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_friend, container, false)
        rc_friends = view.findViewById(R.id.rv_profile)
        rc_friends.layoutManager = LinearLayoutManager(context)

        val retrofitAPI = RetrofitConnection.getInstance().create(FriendService::class.java)
        getFriendList(retrofitAPI)
        friendListLiveData.observe(viewLifecycleOwner, Observer { friendList ->
            profileList.clear() // 변경된 부분: 리스트를 clear 후에 새로운 데이터 추가
            profileList.addAll(friendList)
            adapter_profile.notifyDataSetChanged()
        })

        adapter_profile = ProfileAdapter(profileList)
        rc_friends.adapter = adapter_profile


        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btn_invite).setOnClickListener {
            val inviteFragment = InviteFragment()
            replaceFragment(inviteFragment)
        }

        view.findViewById<Button>(R.id.btn_match).setOnClickListener {
            val matchFragment = MatchFragment()
            replaceFragment(matchFragment)
        }

        adapter_profile.setOnItemClickListener(object : ProfileAdapter.OnItemClickListener {
            override fun onRemoveClick(position: Int, friendId: String) {
                val dialog = ConfirmDialog(
                    requireContext(),
                    "친구 삭제",
                    "친구를 정말 삭제하시겠습니까?",
                    {
                        // 확인 버튼을 눌렀을 때의 처리
                        Log.d("친구 관계id", friendId)

                        val retrofitAPI = RetrofitConnection.getInstance().create(FriendService::class.java)
                        deleteFriend(retrofitAPI, friendId, position)
                    },
                    "확인",
                    0
                )
                dialog.isCancelable = false
                dialog.show(requireActivity().supportFragmentManager, null)
            }
        })
        adapter_profile.setOnGoFriendWishListClickListener(object : ProfileAdapter.OnGoFriendWishListClickListener {
            override fun goFriendList(memberNo: String, name: String) {
                Log.d("친구리스트 클릭 로그","친구번호 : "+memberNo)
                goFriendPage(memberNo, name)
            }
        })


    }
    private fun goFriendPage(memberNo: String, name:String){
        val newFragment = HomeFragment()
        val bundle = Bundle()
        bundle.putString("writerNo", memberNo)
        bundle.putString("isMine", "2")
        Log.d("friendName",name);
        bundle.putString("friendName", name)
        newFragment.arguments = bundle
        replaceFragment(newFragment)
    }
    private fun removeItem(position: Int) {
        profileList.removeAt(position)
        adapter_profile.notifyItemRemoved(position)
    }

    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun getFriendList(retrofitAPI: FriendService) {
        var accessToken = GlobalApplication.prefs.getString("accessToken", "")
        Log.d("loadMyProfileInfo", accessToken)
        accessToken?.let { token ->
            val fragmentContext = requireContext()

            retrofitAPI.getFriendList(token).enqueue(object :
                Callback<FriendListResponse> {
                override fun onResponse(
                    call: Call<FriendListResponse>,
                    response: Response<FriendListResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("getFriendList", "친구 리스트 조회 성공")
                        val friendListResponse = response.body()
                        val friendList = friendListResponse?.data?.map {
                            Profiles(
                                image = it.imgUrl,
                                name = it.nickname,
                                dday = it.matchedToNowDt,
                                remove = R.drawable.remove,
                                friendId = it.friendId,
                                memberId= it.memberId
                            )
                        } ?: emptyList()
                        friendListLiveData.postValue(friendList)
                    } else {
                        Log.d("getFriendList", "친구 리스트 조회 실패")
                    }
                }

                override fun onFailure(call: Call<FriendListResponse>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }


    private fun deleteFriend(retrofitAPI: FriendService, friendId: String, position: Int) {
        retrofitAPI.deleteFriend(friendId).enqueue(object : Callback<FriendDeleteResponse> {
            override fun onResponse(
                call: Call<FriendDeleteResponse>,
                response: Response<FriendDeleteResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("deleteFriend", "친구 삭제하기 성공")
                    val deleteSuccess = response.body()!!.data
                    friendDeleteLiveData.postValue(deleteSuccess)
                    // 성공적으로 삭제된 후에 목록에서 항목을 제거합니다.
                    removeItem(position)
                } else {
                    Log.d("deleteFriend", "친구 삭제하기 실패: ${response.code()}, ${response.message()}")
                }
            }

            override fun onFailure(call: Call<FriendDeleteResponse>, t: Throwable) {
                Log.e("deleteFriend", "친구 삭제하기 실패", t)
                t.printStackTrace()
            }
        })
    }




}
