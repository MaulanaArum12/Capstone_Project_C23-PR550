package com.maulana.netweezen.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.maulana.netweezen.data.Data
import com.maulana.netweezen.R
import com.maulana.netweezen.tweet.TopicDetailActivity

class MainAdapter(private val listData: ArrayList<Data>) : RecyclerView.Adapter<MainAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_menu, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, photo) = listData[position]
        holder.imgPhoto.setImageResource(photo)
        holder.tvName.text = name
        holder.itemView.setOnClickListener {
            if(position == 0){
                val intentTopicDetailActivity = Intent(holder.itemView.context, TopicDetailActivity::class.java)
                intentTopicDetailActivity.putExtra("netweezen", listData[holder.adapterPosition])
                Toast.makeText(
                    holder.itemView.context,
                    "Anda Memilih " + listData[holder.adapterPosition].name,
                    Toast.LENGTH_SHORT
                ).show()
                holder.itemView.context.startActivity(intentTopicDetailActivity)
            }else{
                Toast.makeText(
                    holder.itemView.context,
                    "This Feature Coming Soon",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun getItemCount(): Int = listData.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        val tvName: TextView = itemView.findViewById(R.id.tv_item_name)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Data)
    }
}