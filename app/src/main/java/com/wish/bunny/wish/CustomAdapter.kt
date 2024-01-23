package com.wish.bunny.wish

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wish.bunny.R
import com.wish.bunny.wish.domain.WishItem

class CustomAdapter(private val context: Context, private val wishItemList: List<WishItem>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_wish, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(wishItemList[position])
    }

    override fun getItemCount(): Int {
        return wishItemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val content = itemView.findViewById<TextView>(R.id.rv_content)
        private val dDay = itemView.findViewById<TextView>(R.id.rv_dDay)

        fun bind(wishItem: WishItem) {
            content.text = wishItem.content
            //content.text = wishItem.wishNo
            // dDay 설정 등 필요한 데이터 설정
            // 예시: dDay.text = "D-${calculateDDay(wishItem.deadlineDt)}"
        }

        // 예시: private fun calculateDDay(deadlineDt: String): Int {
        // 예시:   // D-Day를 계산하는 로직을 작성하세요.
        // 예시:   return 0
        // 예시: }
    }
}
