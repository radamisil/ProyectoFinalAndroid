package com.visight.adondevamos.utils

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.visight.adondevamos.R
import com.visight.adondevamos.data.entity.PublicPlace
import kotlinx.android.synthetic.main.layout_dialog_preview.view.*

class PlacePreviewDialog: DialogFragment() {
    var publicPlace: PublicPlace? = null
    var onClickPreviewPlaceDialog: OnClickPreviewPlaceDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            publicPlace = it.get(AppConstants.PUBLIC_PLACE) as PublicPlace?
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v: View = inflater.inflate(R.layout.layout_dialog_preview, container, false)

        v.progressBar.visibility = View.VISIBLE
        v.ivPlaceImage.clipToOutline = true

        if(publicPlace!!.photos != null && !publicPlace!!.photos.isEmpty()){
            GlideApp.with(v.context)
                    .load("https://maps.googleapis.com/maps/api/place/" +
                            "photo?maxwidth=500&photoreference=" +
                            publicPlace!!.photos.get(0).photoReference +
                            "&key=AIzaSyBMfHFvJTPHMgD5zBbRbuJdOjIOJ_HdL4o")
                    .listener(object : RequestListener<Drawable>{
                        override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                        ): Boolean {
                            v.progressBar.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                        ): Boolean {
                            v.progressBar.visibility = View.GONE
                            v.ivPlaceImage.setImageDrawable(resource)
                            return true
                        }

                    })
                    .into(v.ivPlaceImage)
        }else{
            v.progressBar.visibility = View.GONE
            GlideApp.with(v.context)
                    .load(publicPlace!!.icon)
                    .into(v.ivPlaceImage)
        }

        GlideApp.with(v.context)
            .load(publicPlace!!.icon)
            .into(v.ivIcon)

        v.tvName.text = publicPlace!!.name
        v.tvAddress.text = publicPlace!!.vicinity
        v.tvAvailability.text = "Poca concurrencia"

        v.btnGoBack.setOnClickListener {
            this.dismiss()
        }

        v.btnSeeMore.setOnClickListener {
            onClickPreviewPlaceDialog!!.onClickPlaceSeeMore(publicPlace!!)
        }

        return v
    }

    interface OnClickPreviewPlaceDialog{
        fun onClickPlaceSeeMore(publicPlace: PublicPlace)
    }
}