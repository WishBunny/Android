package com.wish.bunny.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.wish.bunny.R
import com.wish.bunny.databinding.ActivityWishListBinding
import com.wish.bunny.retrofit.RetrofitConnection
import com.wish.bunny.wish.CustomAdapter
import com.wish.bunny.wish.WishService
import com.wish.bunny.wish.domain.WishItem
import com.wish.bunny.wish.domain.WishMapResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

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

        loadWishList()

     /*
        view.findViewById<Button>(R.id.rv_detail_btn).setOnClickListener {
            val detailFragment = WishDetailFragment();
            replaceFragment(detailFragment)
        }
        */
    }

    private fun loadWishList() {
        val retrofitAPI = RetrofitConnection.getInstance().create(WishService::class.java)
        retrofitAPI.getWishList().enqueue(object : Callback<WishMapResult> {
            override fun onResponse(call: Call<WishMapResult>, response: Response<WishMapResult>) {
                val wishMapResult = response.body()

                if (wishMapResult != null) {
                    Log.d("WishList", "불러오기 성공: ${wishMapResult.list.size} 개의 아이템")
                    updateUI(wishMapResult.list)
                    Log.d("WishList", wishMapResult.list.toString())
                } else {
                    Log.d("WishList", "서버 응답이 null입니다.")
                }
            }

            override fun onFailure(call: Call<WishMapResult>, t: Throwable) {
                Log.d("WishList", "불러오기 실패: ${t.message}")
                // TODO: 실패 시 처리 구현
            }
        })
    }

    private fun updateUI(wishItemList: List<WishItem>) {
        adapter = CustomAdapter(requireContext(), wishItemList)
        Log.d("wish Context", this.toString())
        binding.wishListRecyclerView.adapter = adapter
        binding.wishListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

}
