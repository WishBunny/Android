package com.wish.bunny.wish

import com.wish.bunny.wish.domain.WishMapResult
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.wish.bunny.databinding.ActivityWishListBinding
import com.wish.bunny.util.RetrofitConnection
import com.wish.bunny.wish.domain.WishItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class WishList : AppCompatActivity() {

    private lateinit var binding: ActivityWishListBinding
    private var  adapter: CustomAdapter? = null
   //  private var wishList = listOf<CustomAdapter.WishModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWishListBinding.inflate(layoutInflater)
        setContentView(binding.root)

       //  setRecyclerView()
        loadWishList() //레트로핏 테스트
    }
    private fun loadWishList() {
        val retrofitAPI = RetrofitConnection.getInstance().create(WishService::class.java)
        retrofitAPI.getWishList().enqueue(object : Callback<WishMapResult> {
            override fun onResponse(call: Call<WishMapResult>, response: Response<WishMapResult>) {
                // 성공 시 처리
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
        adapter = CustomAdapter(this, wishItemList)
        Log.d("wish Context",this.toString());
        binding.wishListRecyclerView.adapter = adapter
        binding.wishListRecyclerView.layoutManager = LinearLayoutManager(this)
    }


    private fun getAllWishList(){
        Thread{
          //  wishList = ArrayList(wishDAO.getAll())
            setRecyclerView()
        }.start()
    }

    private fun setRecyclerView() {
        //adapter = CustomAdapter(this, wishList)
        binding.wishListRecyclerView.adapter = adapter
        binding.wishListRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onRestart() {
        super.onRestart()
        //getAllWishList()
    }
}
