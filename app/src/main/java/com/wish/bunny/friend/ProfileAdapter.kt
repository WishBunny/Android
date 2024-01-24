package com.wish.bunny.friend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wish.bunny.R

class ProfileAdapter(val profileList: ArrayList<Profiles>) : RecyclerView.Adapter<ProfileAdapter.CustomViewHolder>(){

    //클릭 리스너
    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onRemoveClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_friend, parent, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return profileList.size
    }

    override fun onBindViewHolder(holder: ProfileAdapter.CustomViewHolder, position: Int) {
        holder.image.setImageResource(profileList.get(position).image)
        holder.name.text = profileList.get(position).name
        holder.dday.text = profileList.get(position).dday
        holder.remove.setImageResource(profileList.get(position).remove)

        // 삭제 아이콘 클릭 이벤트 처리
        holder.remove.setOnClickListener {
            listener?.onRemoveClick(position)
        }
    }


    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.iv_profile) //프로필 사진
        val name = itemView.findViewById<TextView>(R.id.tv_name) //이름
        val dday = itemView.findViewById<TextView>(R.id.tv_dday) //이메일
        val remove = itemView.findViewById<ImageView>(R.id.iv_remove) //삭제 아이콘
    }

}