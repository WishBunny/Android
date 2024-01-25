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

class HomeFragment : Fragment(), CustomAdapter.OnDetailButtonClickListener {

    private lateinit var binding: ActivityWishListBinding
    private var adapter: CustomAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityWishListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadWishList("NOSET","WRITER002")
        loadDoneWishSize(view, "WRITER002")
        btnClickEvent(view)
        //지금까지 완료한 리스트 확인하기
        val donsListSize = view.findViewById<Button>(R.id.GetDoneButton)
        donsListSize.setOnClickListener {
            loadWishList("Y","WRITER002")
        }
    }

    private fun btnClickEvent(view: View) {
        val button1: Button = view.findViewById(R.id.button1)
        val button2: Button = view.findViewById(R.id.button2)
        val button3: Button = view.findViewById(R.id.button3)

        val pinkColor = ContextCompat.getColor(requireContext(), R.color.pink)
        val changeTextColor = ContextCompat.getColor(requireContext(), R.color.white)
        val transparentColor = ContextCompat.getColor(requireContext(), R.color.ivory)
        val originalTextColor = ContextCompat.getColor(requireContext(), R.color.black) // 원래의 글자색 저장

        button1.setOnClickListener {
            button1.setBackgroundColor(pinkColor) // 핑크색으로 변경
            button1.setTextColor(changeTextColor) // 글자색을 원래대로
            button2.setBackgroundColor(transparentColor) // 다른 버튼은 원래 색으로
            button2.setTextColor(originalTextColor)
            button3.setBackgroundColor(transparentColor)
            button3.setTextColor(originalTextColor)
        }

        button2.setOnClickListener {
            button2.setBackgroundColor(pinkColor)
            button2.setTextColor(changeTextColor) // 글자색을 원래대로
            button1.setBackgroundColor(transparentColor) // 다른 버튼은 원래 색으로
            button1.setTextColor(originalTextColor)
            button3.setBackgroundColor(transparentColor)
            button3.setTextColor(originalTextColor)
        }

        button3.setOnClickListener {
            button3.setBackgroundColor(pinkColor)
            button3.setTextColor(changeTextColor) // 글자색을 원래대로
            button1.setBackgroundColor(transparentColor) // 다른 버튼은 원래 색으로
            button1.setTextColor(originalTextColor)
            button2.setBackgroundColor(transparentColor)
            button2.setTextColor(originalTextColor)
        }
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
                    donsListSize.text="지금까지"+wishMapResult.size.toString()+"개를 이뤘어요!"
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
    private fun loadWishList(achieveYn: String , writerNo: String) {
        val retrofitAPI = RetrofitConnection.getInstance().create(WishService::class.java)
        retrofitAPI.getWishList(achieveYn, writerNo).enqueue(object : Callback<WishMapResult> {
            override fun onResponse(call: Call<WishMapResult>, response: Response<WishMapResult>) {
                val wishMapResult = response.body()

                if (wishMapResult != null) {
                    Log.d("WishList2", "불러오기 성공: ${wishMapResult.list.size} 개의 아이템")
                    updateUI(wishMapResult.list)

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



    private fun updateUI(wishItemList: List<WishItem>) {
        adapter = CustomAdapter(requireContext(), wishItemList)
        adapter?.setOnDetailButtonClickListener(this)

        binding.wishListRecyclerView.adapter = adapter
        binding.wishListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDetailButtonClick(wishNo: String) {
        val newFragment = WishDetailFragment()

        val bundle = Bundle()
        bundle.putString("wishNo", wishNo)
        newFragment.arguments = bundle

        replaceFragment(newFragment)
    }


    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
