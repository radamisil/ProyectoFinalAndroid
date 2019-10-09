package com.visight.adondevamos.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.visight.adondevamos.data.entity.PublicPlace

fun Context.showMessage(message: String, container: ViewGroup){
    DisplayMessage().displayMessage(message, container)
}

fun ImageView.loadImage(context: Context, publicPlace: PublicPlace, progressBar: ProgressBar?){
        GlideApp.with(context)
            .load(Utilities().loadPlaceImageFromGoogle(publicPlace!!.photos!![0].photoReference))
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar?.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar?.visibility = View.GONE
                    setImageDrawable(resource)
                    return true
                }

            })
            .into(this)
    }