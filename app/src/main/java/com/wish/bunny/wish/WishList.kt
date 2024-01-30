package com.wish.bunny.wish

import com.wish.bunny.wish.domain.WishMapResult
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.wish.bunny.databinding.FragmentWishListBinding
import com.wish.bunny.util.RetrofitConnection
import com.wish.bunny.wish.domain.WishItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WishList : AppCompatActivity() {

    private lateinit var binding: FragmentWishListBinding
    private var  adapter: CustomAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentWishListBinding.inflate(layoutInflater)
        setContentView(binding.root)
       //  setRecyclerView()
        loadWishList("NOSET","WRITER002","accessTOKEN","do")//초기 설정
    }
    private fun loadWishList(achieveYn: String, writerNo: String, accessToken: String, category: String) {
        val retrofitAPI = RetrofitConnection.getInstance().create(WishService::class.java)

        val call = retrofitAPI.getWishList(achieveYn, writerNo, "Bearer $accessToken",category)
        call.enqueue(object : Callback<WishMapResult> {
            override fun onResponse(call: Call<WishMapResult>, response: Response<WishMapResult>) {
                val wishMapResult = response.body()

                if (wishMapResult != null) {
                    Log.d("WishList", "불러오기 성공: ${wishMapResult.list.size} 개의 아이템")
                    updateUI(wishMapResult.list, wishMapResult.writerYn)
                    Log.d("WishList", wishMapResult.list.toString())
                } else {
                    Log.d("WishList", "서버 응답이 null입니다.")
                }
            }

            override fun onFailure(call: Call<WishMapResult>, t: Throwable) {
                Log.d("WishList", "불러오기 실패: ${t.message}")
            }
        })
    }

    private fun updateUI(wishItemList: List<WishItem>, writerYn: String) {
        Log.d("wish Context",this.toString());
        binding.wishListRecyclerView.adapter = adapter
        binding.wishListRecyclerView.layoutManager = LinearLayoutManager(this)
    }


    private fun getAllWishList(){
        Thread{
            setRecyclerView()
        }.start()
    }

    private fun setRecyclerView() {
        binding.wishListRecyclerView.adapter = adapter
        binding.wishListRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onRestart() {
        super.onRestart()
    }
}
