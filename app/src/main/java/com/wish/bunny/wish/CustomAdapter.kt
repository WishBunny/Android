package com.wish.bunny.wish

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.wish.bunny.R
import com.wish.bunny.wish.domain.WishItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class CustomAdapter(private val context: Context, private val wishItemList: List<WishItem>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    interface OnDetailButtonClickListener {
        fun onDetailButtonClick(wishNo: String)
    }

    private var onDetailButtonClickListener: OnDetailButtonClickListener? = null

    fun setOnDetailButtonClickListener(listener: OnDetailButtonClickListener) {
        this.onDetailButtonClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_wish, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(wishItemList[position])
        val currentItem = wishItemList[position]
    }

    override fun getItemCount(): Int {
        return wishItemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val content = itemView.findViewById<TextView>(R.id.rv_content)
        private val dDay = itemView.findViewById<TextView>(R.id.rv_dDay)
        private val tag1 = itemView.findViewById<TextView>(R.id.rv_tag1)

        init {
            //컨텐츠 내용 클릭시
            itemView.findViewById<TextView>(R.id.rv_content).setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDetailButtonClickListener?.onDetailButtonClick(wishItemList[position].wishNo)
                }
            }
            //버튼클릭시
            itemView.findViewById<Button>(R.id.rv_detail_btn).setOnClickListener {
                
            }
        }

        fun bind(wishItem: WishItem) {
            val title = wishItem.content
            val wishNo = wishItem.wishNo

            content.text = wishItem.content
            dDay.text = calculateDDay(wishItem.deadlineDt)
            tag1.text = wishItem.tagContents
        }

        private fun calculateDDay(deadlineDt: String): String {
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val targetDate = LocalDate.parse(deadlineDt, dateFormatter)

            //현재날짜
            val currentDate = LocalDate.now()

            //남은 일수 계산
            val daysRemaining = ChronoUnit.DAYS.between(currentDate, targetDate)

            if(daysRemaining>= 0)
                return "D-${daysRemaining.toString()}"
            else
                return "D+${daysRemaining.toString()}"
        }
    }
}
