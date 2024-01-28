package com.wish.bunny.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.wish.bunny.R
import com.wish.bunny.databinding.ActivityWishListBinding
import com.wish.bunny.util.RetrofitConnection
import com.wish.bunny.wish.CustomAdapter
import com.wish.bunny.wish.WishService
import com.wish.bunny.wish.domain.WishItem
import com.wish.bunny.wish.domain.WishMapResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.wish.bunny.GlobalApplication
import com.wish.bunny.mypage.MyPageService
import com.wish.bunny.mypage.domain.ProfileGetResponse

class HomeFragment : Fragment(), CustomAdapter.OnDetailButtonClickListener, CustomAdapter.OnWishCompletedListener {

    private lateinit var binding: ActivityWishListBinding
    private var adapter: CustomAdapter? = null
    private val accessToken = GlobalApplication.prefs.getString("accessToken", "")
    var writerNo = arguments?.getString("writerNo").toString()
    var isMine = arguments?.getString("isMine").toString()
    var friendName = arguments?.getString("friendName").toString()
    var setCategory = "do"
    var setAchieveYn ="n"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isMine = arguments?.getString("isMine").toString()
        writerNo = arguments?.getString("writerNo").toString()
        friendName = arguments?.getString("friendName").toString()
        binding = ActivityWishListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        defaultClick(view)

        if(isMine.equals("1")){
            val retrofitAPI = RetrofitConnection.getInstance().create(MyPageService::class.java)
            loadMyProfileInfo(retrofitAPI, view)
        }
        else{
            loadWishList(setAchieveYn, writerNo.toString(),accessToken,setCategory)
            loadDoneWishSize(view, writerNo.toString())
            btnClickEvent(view)
            //지금까지 완료한 리스트 확인하기
            val donsListSize = view.findViewById<Button>(R.id.GetDoneButton)
            donsListSize.setOnClickListener {
                Log.d("테스트","테스트");
                loadWishList(setAchieveYn, writerNo.toString(),accessToken,setCategory)
            }

            setFriendData(view)
        }
    }
    private fun setFriendData(view: View){
        if(friendName!=null){
            view.findViewById<TextView>(R.id.buketBasText).text = friendName+"님의 버킷 리스트 "
        }
        //지금까지 완료한 리스트 확인하기
        val donsListSize = view.findViewById<Button>(R.id.GetDoneButton)
        donsListSize.setOnClickListener {
            setAchieveYn="Y"
            loadWishList(setAchieveYn, writerNo.toString(),accessToken,setCategory)
        }
    }
    private fun loadMyProfileInfo(retrofitAPI: MyPageService, view: View) {
        Log.d("loadMyProfileInfo", "프로필 정보 불러오기 시도")
        Log.d("loadMyProfileInfo", accessToken)
        accessToken?.let {
            retrofitAPI.loadMyProfile(it).enqueue(object:
                Callback<ProfileGetResponse> {
                override fun onResponse(
                    call: Call<ProfileGetResponse>,
                    response: Response<ProfileGetResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("loadMyProfileInfo", "프로필 정보 불러오기 성공")
                        Log.d("loadMyProfileInfo",response. message());
                        response.body()?.let {
                            setMyProfileInfo(it, view)
                            //프로필 정보를 가져온 후, writerNo를 변경함
                            loadWishList("n", writerNo.toString(),accessToken,"do")
                            loadDoneWishSize(view, writerNo.toString())
                            btnClickEvent(view)
                            //지금까지 완료한 리스트 확인하기
                            val donsListSize = view.findViewById<Button>(R.id.GetDoneButton)
                            donsListSize.setOnClickListener {
                                setAchieveYn="Y"
                                loadWishList(setAchieveYn, writerNo.toString(),accessToken,setCategory)
                            }
                        }
                    } else {
                        Log.d("loadMyProfileInfo", "프로필 정보 불러오기 시도")
                        Log.d("loadMyProfileInfo", "프로필 정보 불러오기 실패")
                    }
                }
                override fun onFailure(call: Call<ProfileGetResponse>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }


    private fun defaultClick(view: View){
        val pinkColor = ContextCompat.getColor(requireContext(), R.color.pink)
        val changeTextColor = ContextCompat.getColor(requireContext(), R.color.white)
        val transparentColor = ContextCompat.getColor(requireContext(), R.color.wishbunny_background)
        val originalTextColor = ContextCompat.getColor(requireContext(), R.color.black) // 원래의 글

        binding.button1.setBackgroundColor(pinkColor) // 핑크색으로 변경
        binding.button1.setTextColor(changeTextColor) // 글자색을 원래대로
        binding.button2.setBackgroundColor(transparentColor) // 다른 버튼은 원래 색으로
        binding.button2.setTextColor(originalTextColor)
        binding.button3.setBackgroundColor(transparentColor)
        binding.button3.setTextColor(originalTextColor)
    }
    private fun btnClickEvent(view: View) {
        val pinkColor = ContextCompat.getColor(requireContext(), R.color.pink)
        val changeTextColor = ContextCompat.getColor(requireContext(), R.color.white)
        val transparentColor = ContextCompat.getColor(requireContext(), R.color.wishbunny_background)
        val originalTextColor = ContextCompat.getColor(requireContext(), R.color.black) // 원래의 글자색 저장

        binding.button1.setOnClickListener {
            binding.button1.setBackgroundColor(pinkColor) // 핑크색으로 변경
            binding.button1.setTextColor(changeTextColor) // 글자색을 원래대로
            binding.button2.setBackgroundColor(transparentColor) // 다른 버튼은 원래 색으로
            binding.button2.setTextColor(originalTextColor)
            binding.button3.setBackgroundColor(transparentColor)
            binding.button3.setTextColor(originalTextColor)
            setCategory ="do"
            if(writerNo!= null){
                loadWishList(setAchieveYn, writerNo.toString(),accessToken,setCategory)
            }
        }

        binding.button2.setOnClickListener {
            binding.button2.setBackgroundColor(pinkColor)
            binding.button2.setTextColor(changeTextColor) // 글자색을 원래대로
            binding.button1.setBackgroundColor(transparentColor) // 다른 버튼은 원래 색으로
            binding.button1.setTextColor(originalTextColor)
            binding.button3.setBackgroundColor(transparentColor)
            binding.button3.setTextColor(originalTextColor)
            setCategory ="eat"
            if(writerNo!= null){
                loadWishList(setAchieveYn, writerNo.toString(),accessToken,setCategory)
            }

        }

        binding.button3.setOnClickListener {
            binding.button3.setBackgroundColor(pinkColor)
            binding.button3.setTextColor(changeTextColor) // 글자색을 원래대로
            binding.button1.setBackgroundColor(transparentColor) // 다른 버튼은 원래 색으로
            binding.button1.setTextColor(originalTextColor)
            binding.button2.setBackgroundColor(transparentColor)
            binding.button2.setTextColor(originalTextColor)
            setCategory ="get"
            if(writerNo!= null){
                loadWishList(setAchieveYn, writerNo.toString(),accessToken,setCategory)
            }

        }
    }
    private fun setMyProfileInfo(it: ProfileGetResponse, view: View) : String {
        Log.d("setMyProfileInfo",it.data.toString())
        view.findViewById<TextView>(R.id.buketBasText).text = it.data.nickname+"님의 버킷 리스트 "
        writerNo = it.data.memberId
        Log.d("writerNo: ", writerNo.toString());
        return it.data.memberId
    }
    //위시리스트 완료처리한것의 갯수 보여주기
    private fun loadDoneWishSize(view: View, writerNo :String){
        val retrofitAPI = RetrofitConnection.getInstance().create(WishService::class.java)
        retrofitAPI.doneListSize(writerNo).enqueue(object : Callback<WishMapResult> {
            override fun onResponse(call: Call<WishMapResult>, response: Response<WishMapResult>) {
                val wishMapResult = response.body()

                if (wishMapResult != null) {
                    Log.d("loadDoneWishSize", wishMapResult.size.toString())
                    // GetDoneButton을 찾아 텍스트 변경
                    val donsListSize = view.findViewById<Button>(R.id.GetDoneButton)
                    donsListSize.text="지금까지 "+wishMapResult.size.toString()+"개를 이뤘어요!"
                } else {
                    Log.d("loadDoneWishSize", "서버 응답이 null입니다.")
                }
            }

            override fun onFailure(call: Call<WishMapResult>, t: Throwable) {
                Log.d("loadDoneWishSize", "불러오기 실패: ${t.message}")
                // TODO: 실패 시 처리 구현
            }
        })
    }
    private fun loadWishList(achieveYn: String, writerNo: String, accessToken: String, category:String) {
        val retrofitAPI = RetrofitConnection.getInstance().create(WishService::class.java)

        val call = retrofitAPI.getWishList(achieveYn, writerNo, "$accessToken",category)
        call.enqueue(object : Callback<WishMapResult> {
            override fun onResponse(call: Call<WishMapResult>, response: Response<WishMapResult>) {
                val wishMapResult = response.body()

                if (wishMapResult != null) {
                    Log.d("WishList2", "불러오기 성공: ${wishMapResult.list.size} 개의 아이템")
                    Log.d("writerYn",wishMapResult.writerYn)
                    updateUI(wishMapResult.list, wishMapResult.writerYn)

                    Log.d("WishList2", wishMapResult.list.toString())
                } else {
                    Log.d("WishList2", "서버 응답이 null입니다.")
                }
            }

            override fun onFailure(call: Call<WishMapResult>, t: Throwable) {
                Log.d("WishList2", "불러오기 실패: ${t.message}")
                // TODO: 실패 시 처리 구현
            }
        })
    }

    private fun updateUI(wishItemList: List<WishItem>, writerYn: String) {
        adapter = CustomAdapter(requireContext(), wishItemList, writerYn, this)
        adapter?.setOnDetailButtonClickListener(this)

        binding.wishListRecyclerView.adapter = adapter
        binding.wishListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }




    override fun onDetailButtonClick(wishNo: String) {
        val newFragment = WishDetailFragment()

        val bundle = Bundle()
        bundle.putString("wishNo", wishNo)
        // bundle.putString("writerNo", memberNo)
        newFragment.arguments = bundle

        replaceFragment(newFragment)
    }


    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onWishCompleted() {
        Log.d("onWishCompleted","onWishCompleted 클릭");
        loadDoneWishSize(requireView(), writerNo.toString())
        setAchieveYn="Y"
        loadWishList(setAchieveYn, writerNo.toString(),accessToken,setCategory)
    }
}