package com.visight.adondevamos.ui.main.mapView.mapUtils

import android.content.Context
import android.graphics.Color
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.visight.adondevamos.R

class MyMapClusterRenderer(context: Context?, map: GoogleMap?, clusterManager: ClusterManager<MapItem>?) :
    DefaultClusterRenderer<MapItem>(context, map, clusterManager) {
    val color: String = "#FFAB00"  //color Accent

    override fun onBeforeClusterItemRendered(item: MapItem?, markerOptions: MarkerOptions?) {
        markerOptions!!.icon(BitmapDescriptorFactory.fromResource(R.drawable.img_marker_available))
    }

    override fun shouldRenderAsCluster(cluster: Cluster<MapItem>?): Boolean {
        return cluster!!.size > 4
    }

    override fun getColor(clusterSize: Int): Int {
        return Color.parseColor(color)
    }
}