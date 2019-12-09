package com.visight.adondevamos.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.visight.adondevamos.R
import com.visight.adondevamos.data.entity.PublicPlace
import kotlinx.android.synthetic.main.layout_dialog_preview.view.*

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

fun ImageView.loadTintedIcon(publicPlace: PublicPlace){
    Glide.with(this.context)
        .asBitmap()
        .load(publicPlace.icon)
        .into(object: CustomViewTarget<ImageView, Bitmap>(this){
            override fun onLoadFailed(errorDrawable: Drawable?) {

            }

            override fun onResourceCleared(placeholder: Drawable?) {

            }

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                val paint = Paint()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    paint.colorFilter =
                        PorterDuffColorFilter(resources.getColor(R.color.colorAccent, null),
                            PorterDuff.Mode.SRC_IN)
                }
                val bitmapResult = Bitmap.createBitmap(resource.width, resource.height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmapResult)
                canvas.drawBitmap(resource, 0f, 0f, paint)
                this@loadTintedIcon.setImageBitmap(bitmapResult)
            }
        })
}