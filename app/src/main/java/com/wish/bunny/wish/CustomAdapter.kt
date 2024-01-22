package com.wish.bunny.wish

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wish.bunny.R

class CustomAdapter(private val context: Context, private val dataList: List<WishModel>) :
    RecyclerView.Adapter<CustomAdapter.WishHolder>() {

    class WishHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rvContentTextView: TextView = itemView.findViewById(R.id.rv_content)
        val rvDDayTextView: TextView = itemView.findViewById(R.id.rv_dDay)
        val rvTag1TextView: TextView = itemView.findViewById(R.id.rv_tag1)
        val rvTag2TextView: TextView = itemView.findViewById(R.id.rv_tag2)
        val rvDoneButton: Button = itemView.findViewById(R.id.rv_done_btn)
    }

    //추후에 수정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_wish, parent, false)
        return WishHolder(view)
    }

    override fun onBindViewHolder(holder: WishHolder, position: Int) {
        val item = dataList[position]

        holder.rvContentTextView.text = item.content
        holder.rvDDayTextView.text = item.dDay
        holder.rvTag1TextView.text = item.tag1
        holder.rvTag2TextView.text = item.tag2


        holder.rvDoneButton.setOnClickListener {
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    data class WishModel(
        val content: String,
        val dDay: String,
        val tag1: String,
        val tag2: String
    )
}
