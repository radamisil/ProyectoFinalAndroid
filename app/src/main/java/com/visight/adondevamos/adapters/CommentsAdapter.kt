package com.visight.adondevamos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.visight.adondevamos.R
import com.visight.adondevamos.data.entity.Comment
import com.visight.adondevamos.utils.GlideApp
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentsAdapter(var items: List<Comment>): RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun onBind(comment: Comment){
            itemView.ivProfileImage.clipToOutline = true
            GlideApp.with(itemView.context)
                .load(comment.user!!.profileImage)
                .placeholder(R.drawable.logo)
                .into(itemView.ivProfileImage)

            itemView.tvName.text = "${comment.user!!.name} ${comment.user!!.surname}"
            itemView.rbStars.rating = comment.ratingReported!!
            itemView.tvRatingReported.text = comment.ratingReported!!.toString()
            itemView.tvComment.text = comment.comment!!
            itemView.tvDate.text = comment.dateReported!!
        }
    }

}