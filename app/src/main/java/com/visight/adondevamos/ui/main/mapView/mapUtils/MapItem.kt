package com.visight.adondevamos.ui.main.mapView.mapUtils

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.visight.adondevamos.data.entity.PublicPlace

class MapItem(/*place: PublicPlace, */location: LatLng) : ClusterItem {
    var publicPlace : PublicPlace? = null
    var publicPlacePosition : LatLng? = null

    init {
        //publicPlace = place
        publicPlacePosition = location
    }

    override fun getSnippet(): String? {
        return null
    }

    override fun getTitle(): String? {
        return null
    }

    override fun getPosition(): LatLng {
        return publicPlacePosition!!
    }
}