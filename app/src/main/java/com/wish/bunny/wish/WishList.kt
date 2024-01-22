package com.wish.bunny.wish

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.wish.bunny.R
import com.wish.bunny.databinding.ActivityWishListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WishList : AppCompatActivity() {

    private lateinit var binding: ActivityWishListBinding
    private lateinit var adapter: CustomAdapter
    private var wishList = listOf<CustomAdapter.WishModel>() // Assuming YourItemModel is the data class in your CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWishListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setRecyclerView()
    }
    private fun getAllWishList(){
        Thread{
          //  wishList = ArrayList(wishDAO.getAll())
            setRecyclerView()
        }.start()
    }

    private fun setRecyclerView() {
        adapter = CustomAdapter(this, wishList)
        binding.wishListRecyclerView.adapter = adapter
        binding.wishListRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onRestart() {
        super.onRestart()
        getAllWishList()
    }
}
