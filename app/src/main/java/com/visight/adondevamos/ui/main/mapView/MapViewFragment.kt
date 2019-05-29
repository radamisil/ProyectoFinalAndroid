package com.visight.adondevamos.ui.main.mapView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.visight.adondevamos.R
import kotlinx.android.synthetic.main.fragment_map.*

class MapViewFragment : Fragment(), OnMapReadyCallback{
    private lateinit var mMap: GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //TODO PREGUNTAR POR QUÉ CUANDO COMENTO ESTO ANDA, PERO SI LO DESCOMENTO DA NULL Y SE ROMPE?
        /*val mapFragment = activity!!.supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)*/
        return inflater.inflate(R.layout.fragment_map, container, false)
    }
    override fun onMapReady(googleMap: GoogleMap?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        mMap = googleMap!!
    }
}