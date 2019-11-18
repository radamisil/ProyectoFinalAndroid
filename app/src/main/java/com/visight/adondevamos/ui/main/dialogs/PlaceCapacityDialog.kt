package com.visight.adondevamos.ui.main.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.visight.adondevamos.R
import kotlinx.android.synthetic.main.layout_dialog_capacity.view.*

class PlaceCapacityDialog: DialogFragment() {
    var onClickSendPlaceCapacity: OnClickSendPlaceCapacity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v: View = inflater.inflate(R.layout.layout_dialog_capacity, container, false)

        v.btnGoBack.setOnClickListener {
            this.dismiss()
        }

        v.btnSend.setOnClickListener {
            onClickSendPlaceCapacity!!.onClickSendPlaceCapacity()
        }

        return v
    }

    interface OnClickSendPlaceCapacity{
        fun onClickSendPlaceCapacity()
    }
}