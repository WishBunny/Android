package com.wish.bunny.wish

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wish.bunny.R
import com.wish.bunny.wish.domain.WishItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

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
        private val tag1 = itemView.findViewById<TextView>(R.id.rv_tag1)

        fun bind(wishItem: WishItem) {
            content.text = wishItem.content
            dDay.text = calculateDDay(wishItem.deadlineDt)
            tag1.text = wishItem.tagContents
            //content.text = wishItem.wishNo
            // dDay 설정 등 필요한 데이터 설정
            // 예시: dDay.text = "D-${calculateDDay(wishItem.deadlineDt)}"
        }

        private fun calculateDDay(deadlineDt: String): String {
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val targetDate = LocalDate.parse(deadlineDt, dateFormatter)

            //현재날짜
            val currentDate = LocalDate.now()

            //남은 일수 계산
            val daysRemaining = ChronoUnit.DAYS.between(currentDate, targetDate)

            return "D-${daysRemaining.toString()}"
        }
    }
}
