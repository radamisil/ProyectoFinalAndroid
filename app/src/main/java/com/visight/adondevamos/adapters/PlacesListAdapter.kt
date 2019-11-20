package com.visight.adondevamos.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.visight.adondevamos.R
import com.visight.adondevamos.data.entity.PublicPlace
import com.visight.adondevamos.ui.main.listView.ListViewFragment
import com.visight.adondevamos.ui.main.place.PlaceDetailActivity
import com.visight.adondevamos.utils.AppConstants
import com.visight.adondevamos.utils.GlideApp
import com.visight.adondevamos.utils.Utilities
import kotlinx.android.synthetic.main.item_place.view.*

class PlacesListAdapter(var items: List<PublicPlace>) : RecyclerView.Adapter<PlacesListAdapter.ViewHolder>() {
    var onClickPlaceItem: OnClickPlaceItem? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(place: PublicPlace){
            itemView.ivPlaceImage.clipToOutline = true
            itemView.ivPlaceIcon.clipToOutline = true

            //TODO change placeholder
            if(place.photos != null && place.photos!!.isNotEmpty()){
                GlideApp.with(itemView.context)
                    .load(Utilities().loadPlaceImageFromGoogle(place!!.photos!![0].photoReference))
                    .placeholder(R.drawable.logo)
                    .into(itemView.ivPlaceImage)
            }else{
                GlideApp.with(itemView.context)
                    .load(R.drawable.logo)
                    .into(itemView.ivPlaceImage)
            }

            if(place.icon != null){
                GlideApp.with(itemView.context)
                    .load(place.icon)
                    .placeholder(R.drawable.logo)
                    .into(itemView.ivPlaceIcon)
            }else{
                GlideApp.with(itemView.context)
                    .load(R.drawable.ic_location)
                    .into(itemView.ivPlaceIcon)
            }

            itemView.tvPlaceName.text = place.name
            itemView.rbStars.rating = if(place.rating != null) place.rating!!.toFloat() else 0f
            itemView.tvPlaceRating.text = if(place.rating != null) place.rating!!.toString() else "0.0"
            itemView.tvPlaceAddress.text = place.vicinity

            //TODO hardcoded
            itemView.tvAvailabilityHuman.text = "Baja"
            itemView.tvAvailabilityAI.text = "Media"

            //TODO remove from Adapter
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, PlaceDetailActivity::class.java)
                intent.putExtra(AppConstants.PUBLIC_PLACE, place)
                startActivity(itemView.context, intent, null)
            }
        }
    }

    interface OnClickPlaceItem{
        fun onClickPlaceItem(placeId: Int)
    }
}