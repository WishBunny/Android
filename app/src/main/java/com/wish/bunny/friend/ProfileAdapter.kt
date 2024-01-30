package com.wish.bunny.friend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wish.bunny.R
import com.wish.bunny.friend.domain.Profiles

/**
작성자: 이혜연
처리 내용: ProfileAdapter 구현
 */
class ProfileAdapter(private var profileList: List<Profiles>) :
    RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {
    private var onItemClickListener: OnItemClickListener? = null
    private var onGoFriendWishListClickListener: OnGoFriendWishListClickListener? = null

    interface OnItemClickListener {
        fun onRemoveClick(position: Int, friendId: String)
    }

    interface OnGoFriendWishListClickListener {
        fun goFriendList(memberNo: String, name: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    fun setOnGoFriendWishListClickListener(listener: OnGoFriendWishListClickListener) {
        this.onGoFriendWishListClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_friend, parent, false)
        return ProfileViewHolder(view)
    }

    override fun getItemCount(): Int {
        return profileList.size
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val currentItem = profileList[position]

        holder.itemView.findViewById<TextView>(R.id.tv_name).text = currentItem.name
        holder.itemView.findViewById<TextView>(R.id.tv_dday).text = currentItem.dday

        Glide.with(holder.itemView.context)
            .load(currentItem.image)
            .placeholder(R.drawable.default_profile)
            .into(holder.itemView.findViewById(R.id.iv_profile))

        holder.itemView.findViewById<ImageView>(R.id.iv_remove).setOnClickListener {
            onItemClickListener?.onRemoveClick(position, currentItem.friendId)
        }
        holder.itemView.findViewById<TextView>(R.id.tv_name).setOnClickListener {
            onGoFriendWishListClickListener?.goFriendList(currentItem.memberId, currentItem.name)
        }
    }

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProfile: ImageView = itemView.findViewById(R.id.iv_profile)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvDday: TextView = itemView.findViewById(R.id.tv_dday)
        val ivRemove: ImageView = itemView.findViewById(R.id.iv_remove)
    }

}
