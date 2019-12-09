package com.visight.adondevamos.ui.main.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.visight.adondevamos.R
import com.visight.adondevamos.data.entity.PublicPlace
import com.visight.adondevamos.data.remote.responses.PollAverageResponse
import com.visight.adondevamos.utils.AppConstants
import com.visight.adondevamos.utils.GlideApp
import com.visight.adondevamos.utils.loadImage
import kotlinx.android.synthetic.main.layout_dialog_preview.view.*
import com.visight.adondevamos.utils.loadTintedIcon


class PlacePreviewDialog: DialogFragment() {
    var publicPlace: PublicPlace? = null
    var pollAverageResponse: PollAverageResponse? = null
    var onClickPreviewPlaceDialog: OnClickPreviewPlaceDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            publicPlace = it.get(AppConstants.PUBLIC_PLACE) as PublicPlace?
            pollAverageResponse = it.get(AppConstants.PUBLIC_PLACE_CURRENT_AVAILABILITY) as PollAverageResponse?
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v: View = inflater.inflate(R.layout.layout_dialog_preview, container, false)

        v.progressBar.visibility = View.VISIBLE
        v.ivPlaceImage.clipToOutline = true

        if(publicPlace!!.photos != null && publicPlace!!.photos!!.isNotEmpty()){
            v.ivPlaceImage.loadImage(v.context, publicPlace!!, v.progressBar)
        }else{
            v.progressBar.visibility = View.GONE
            v.ivPlaceImage.loadTintedIcon(publicPlace!!)
            /*GlideApp.with(v.context)
                    .load(publicPlace!!.icon)
                    .into(v.ivPlaceImage)*/
        }

        /*GlideApp.with(v.context)
            .load(publicPlace!!.icon)
            .into(v.ivIcon)*/
        v.ivIcon.loadTintedIcon(publicPlace!!)

        v.tvName.text = publicPlace!!.name
        v.tvAddress.text = publicPlace!!.vicinity

        //TODO AVAILABILITY - add API results
        v.tvAvailabilityHumanQuantity.text = pollAverageResponse!!.IA.toString() + "%"
        v.tvAvailabilityAIQuantity.text = pollAverageResponse!!.encuesta.toString() + "%"
        v.tvAvailabilityHuman.text = "Baja"
        v.tvAvailabilityAI.text = "Media"

        //TODO edit Promotions visibility
        if(!publicPlace!!.promotions.isNullOrEmpty())
            v.clContainerPromotion.visibility = View.VISIBLE
        else
            //v.clContainerPromotion.visibility = View.GONE
            v.clContainerPromotion.visibility = View.VISIBLE

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