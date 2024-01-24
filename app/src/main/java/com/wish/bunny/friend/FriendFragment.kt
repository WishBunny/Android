package com.wish.bunny.friend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wish.bunny.R

class FriendFragment : Fragment() {
    lateinit var rc_friends: RecyclerView
    lateinit var adapter_profile: RecyclerView.Adapter<*>
    var profileList: ArrayList<Profiles> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_friend, container, false)
        rc_friends = view.findViewById(R.id.rv_profile)
        rc_friends.layoutManager = LinearLayoutManager(context)

        profileList = arrayListOf(
            Profiles(R.drawable.profile, "혜연", "버니된지 D+1", R.drawable.remove),
            Profiles(R.drawable.profile, "차은우", "버니된지 D+5", R.drawable.remove),
            Profiles(R.drawable.profile, "강동원", "버니된지 D+13", R.drawable.remove),
            Profiles(R.drawable.profile, "박형식", "버니된지 D+100", R.drawable.remove),
            Profiles(R.drawable.profile, "설영우", "버니된지 D+300", R.drawable.remove),
            Profiles(R.drawable.profile, "황민현", "버니된지 D+139", R.drawable.remove),
            Profiles(R.drawable.profile, "혜연", "버니된지 D+500", R.drawable.remove),
            Profiles(R.drawable.profile, "혜연", "버니된지 D+180", R.drawable.remove)
        )

        adapter_profile = ProfileAdapter(profileList)
        rc_friends.adapter = adapter_profile

        adapter_profile.notifyDataSetChanged()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btn_invite).setOnClickListener {
            val inviteFragment = InviteFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_friend, inviteFragment)
                .addToBackStack(null) //이전 프래그먼트로 돌아가기
                .commit()
        }

        view.findViewById<Button>(R.id.btn_match).setOnClickListener {
            // 버튼 매치에 대한 처리 추가
        }


    }
}