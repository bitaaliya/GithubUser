package com.dicoding.socialsnap.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dicoding.socialsnap.R
import com.dicoding.socialsnap.data.response.ItemsItem

class SocialAdapter(private var listUser: List<ItemsItem>) : RecyclerView.Adapter<SocialAdapter.ViewHolder>() {

    private lateinit var onItemClickCallBack: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: ItemsItem)
    }

    fun setOnItemClickCallback(onItemClickCallBack: OnItemClickCallback) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_social, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val user = listUser[position]
        viewHolder.tvUsername.text = user.login
        Glide.with(viewHolder.itemView.context)
            .load(user.avatarUrl)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(viewHolder.imgAvatar)
        viewHolder.itemView.setOnClickListener {
            onItemClickCallBack.onItemClicked(user)
        }
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    fun updateData(newList: List<ItemsItem>) {
        listUser = newList
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUsername: TextView = view.findViewById(R.id.tv_username)
        val imgAvatar: ImageView = view.findViewById(R.id.img_avatar)
    }
}


