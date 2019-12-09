package com.visight.adondevamos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.visight.adondevamos.R
import com.visight.adondevamos.data.entity.Promotion
import kotlinx.android.synthetic.main.item_promotion.view.*

class PromotionsAdapter(var items: List<Promotion>) : RecyclerView.Adapter<PromotionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_promotion, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun onBind(promotion: Promotion){
            itemView.tvDescription.text = promotion.description
            itemView.tvPrice.text = "$ ${promotion.price}"
        }
    }

    fun updatePromotionsList(promotions: List<Promotion>){
        this.items = promotions
        notifyDataSetChanged()
    }

}