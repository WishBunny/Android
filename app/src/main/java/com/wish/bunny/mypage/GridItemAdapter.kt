package com.wish.bunny.mypage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wish.bunny.R
import com.wish.bunny.mypage.domain.GridItem

class GridItemAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val listData: ArrayList<GridItem> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_grid, parent, false)
        return GridItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as GridItemViewHolder).onBind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    fun addItem(data: GridItem) {
        listData.add(data)
    }
}