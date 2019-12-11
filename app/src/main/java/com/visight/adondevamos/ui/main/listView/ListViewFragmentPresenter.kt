package com.visight.adondevamos.ui.main.listView

import com.google.android.gms.maps.model.LatLng
import com.visight.adondevamos.data.entity.PublicPlace
import com.visight.adondevamos.data.remote.GooglePlacesService
import com.visight.adondevamos.data.remote.responses.GetPublicPlacesResponse
import com.visight.adondevamos.ui.main.mapView.mapUtils.MapItem
import com.visight.adondevamos.utils.AppConstants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ListViewFragmentPresenter : ListViewFragmentContract.Presenter {
    private var mView: ListViewFragmentContract.View? = null
    private var allPublicPlacesList: MutableList<PublicPlace>? = mutableListOf()
    var disposable: Disposable? = null
    var allPlaces: MutableList<PublicPlace>? = mutableListOf()
    var allPublicPlaces: MutableList<PublicPlace>? = mutableListOf()
    var placesToDisplay: MutableList<PublicPlace>? = mutableListOf()
    var placeTypesList: HashMap<String, String> = hashMapOf()

    override fun startView(view: ListViewFragmentContract.View) {
        mView = view
        initPlaceTypesList()
    }

    override fun onDestroy() {
        mView = null
    }

    private fun initPlaceTypesList() {
        placeTypesList = hashMapOf(
            "Comercios" to "store",
            "Restaurantes" to "restaurant",
            "Supermercados" to "supermarket",
            "Shoppings" to "shopping_mall",
            "Cajeros automáticos" to "atm",
            "Panaderías" to "bakery",
            "Bancos" to "bank",
            "Bares" to "bar",
            "Salones de belleza" to "beauty_salon",
            "Librerías" to "book_store",
            "Cafés" to "cafe",
            "Reparación de autos" to "car_repair",
            "Lavaderos de autos" to "car_wash",
            "Casinos" to "casino",
            "Dentistas" to "dentist",
            "Doctores" to "doctor",
            "Estaciones de servicio" to "gas_station",
            "Gimnasios" to "gym",
            "Peluquerías" to "hair_care",
            "Hospitales" to "hospital",
            "Lavaderos" to "laundry",
            "Museos" to "museum",
            "Parques" to "park",
            "Farmacias" to "pharmacy"
        )
    }

    override fun getPublicPlaces(location: LatLng, type: String?) {
        val locationToSend: String = location.latitude.toString() + "," + location.longitude.toString()

        //type is null; first time I ask for all places
        if (type == null) {
            disposable = GooglePlacesService().getClient().getPublicPlaces(
                locationToSend, AppConstants.searchRadius, "",
                "AIzaSyBMfHFvJTPHMgD5zBbRbuJdOjIOJ_HdL4o"
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map { t: GetPublicPlacesResponse ->
                    t.results
                }
                .toObservable()  //TODO PREGUNTAR SI ESTO ERA NECESARIO
                .flatMapIterable { l -> l }
                .toList()
                .subscribe { items: List<PublicPlace>?, throwable: Throwable? ->
                    run {
                        if (items != null) {
                            allPublicPlaces?.clear()
                            allPlaces?.addAll(items)
                            for (i in allPlaces!!) {
                                allPublicPlaces!!.add(i)
                                for (t in i.types) {
                                    if (t == "store") {
                                        placesToDisplay?.add(i)
                                    }
                                }
                            }

                            mView!!.displayPlaces(placesToDisplay!!)
                        } else {
                            throwable!!.message?.let {
                                mView!!.displayMessage(it)
                            }
                        }
                    }
                }
        } else {
            //placesToDisplay?.clear()

            var selectedPlaceType = ""
            for (t in placeTypesList) {
                if (t.key == type) {
                    selectedPlaceType = t.value
                    break
                }
            }

            for (i in allPlaces!!) {
                for (t in i.types) {
                    if (t == selectedPlaceType) {
                        placesToDisplay?.add(i)
                    }
                }
            }
            mView!!.displayPlaces(placesToDisplay!!)
        }
    }

    override fun setPublicPlacesList(list: List<PublicPlace>) {
        allPublicPlacesList!!.addAll(list)
    }

    override fun getStoredPlacesList(): List<PublicPlace> {
        return allPublicPlacesList!!
    }

}