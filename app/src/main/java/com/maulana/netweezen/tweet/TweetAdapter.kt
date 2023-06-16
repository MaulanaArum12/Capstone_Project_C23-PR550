package com.maulana.netweezen.tweet

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.maulana.netweezen.R

class TweetAdapter (private val listReview: List<String>, private val listClean: List<String>, private val listUsername: List<String>, private val listScore: List<String>, private val listLike: List<String>, private val listComment: List<String>) : RecyclerView.Adapter<TweetAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_detail, viewGroup, false))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.tvItem.text = listReview[position]

        viewHolder.itemView.setOnClickListener {

            val intentMoreActivity = Intent(viewHolder.itemView.context, TweetDetailActivity::class.java)
            intentMoreActivity.putExtra("review", listReview[viewHolder.adapterPosition])
            intentMoreActivity.putExtra("clean", listClean[viewHolder.adapterPosition])
            intentMoreActivity.putExtra("username", listUsername[viewHolder.adapterPosition])
            intentMoreActivity.putExtra("score", listScore[viewHolder.adapterPosition])
            intentMoreActivity.putExtra("like", listLike[viewHolder.adapterPosition])
            intentMoreActivity.putExtra("comment", listComment[viewHolder.adapterPosition])
            Log.d("NETWEEZEN", "onBindViewHolder: " + listUsername[viewHolder.adapterPosition])
            viewHolder.itemView.context.startActivity(intentMoreActivity)
        }
    }

    override fun getItemCount() = listReview.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvItem: TextView = view.findViewById(R.id.tv_item_description)
    }
    interface OnItemClickCallback {
        fun onItemClicked()
    }
}