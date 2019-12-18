package com.visight.adondevamos.ui.main.place

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.visight.adondevamos.R
import com.visight.adondevamos.adapters.CommentsAdapter
import com.visight.adondevamos.ui.base.BaseActivity
import com.visight.adondevamos.ui.main.place.report.ReportFromPlaceActivity
import kotlinx.android.synthetic.main.activity_place_detail.*
import kotlinx.android.synthetic.main.layout_place_detail_content.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.math.RoundingMode
import android.R.attr.data
import android.content.SharedPreferences
import android.content.res.Resources
import com.github.mikephil.charting.charts.LineChart
import android.graphics.Color
import android.os.Build
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.visight.adondevamos.adapters.HistoryInformationAdapter
import com.visight.adondevamos.adapters.PromotionsAdapter
import com.visight.adondevamos.data.entity.*
import com.visight.adondevamos.data.local.SharedPreferencesManager
import com.visight.adondevamos.data.remote.responses.PollAverageResponse
import com.visight.adondevamos.utils.*
import kotlinx.android.synthetic.main.layout_dialog_preview.view.*


class PlaceDetailActivity : BaseActivity(), PlaceDetailActivityContract.View {
    var mPresenter: PlaceDetailActivityContract.Presenter? = null
    var mPublicPlace: PublicPlace? = null
    var mPublicPlaceCurrentAvailability: PollAverageResponse? = null
    var mPromotionsAdapter: PromotionsAdapter? = null
    var placeIsFaved: Boolean = false
    var pieChartPagerAdapter: HistoryInformationAdapter? = null

    //profile images
    var person1 =
        "https://www.google.com/imgres?imgurl=https%3A%2F%2Ffsmedia.imgix.net%2Fca%2Fae%2F25%2F9c%2Fb6f8%2F476e%2Fb006%2F0afd6ca06e33%2Fscreen-shot-2019-02-13-at-25638-pmpng.png%3Fauto%3Dcompress%26h%3D675%26w%3D1200%26crop%3Dedges%26fit%3Dcrop&imgrefurl=https%3A%2F%2Fwww.inverse.com%2Farticle%2F53280-this-person-does-not-exist-gans-website&docid=8lhLZgIzKWeTaM&tbnid=nYH9CDYE3y2IoM%3A&vet=10ahUKEwjajL_RhZDlAhXGJbkGHS66CbUQMwhMKAIwAg..i&w=1200&h=675&bih=638&biw=1345&q=person%20face&ved=0ahUKEwjajL_RhZDlAhXGJbkGHS66CbUQMwhMKAIwAg&iact=mrc&uact=8"
    var person2 = "https://www.pexels.com/photo/face-facial-hair-fine-looking-guy-614810/"
    var person3 = "https://unsplash.com/s/photos/man-face"
    var person4 = "https://pixabay.com/photos/face-the-person-men-s-1389832/"
    var person5 = "http://www.whichfaceisreal.com/fakeimages/image-2019-02-17_020449.jpeg"
    var person6 =
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS6-_swo2nPuVu92bxYEXENm3l-NaPUrfLoAN0y0YH2FQSSkZTuyA"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)
        startPresenter()

        mPublicPlace = intent.getParcelableExtra(AppConstants.PUBLIC_PLACE)
        mPublicPlaceCurrentAvailability = intent.getParcelableExtra(AppConstants.PUBLIC_PLACE_CURRENT_AVAILABILITY)

        setUpToolbar(toolbar, mPublicPlace!!.name, ivLogo)
        setPlaceInformation(mPublicPlace!!)

        if (mPublicPlace!!.photos != null && mPublicPlace!!.photos!!.isNotEmpty()) {
            ivHeader.loadImage(this, mPublicPlace!!, progressBarPlaceDetailImage)
        } else {
            progressBarPlaceDetailImage.visibility = View.GONE
            /*GlideApp.with(this)
                .load(mPublicPlace!!.icon)
                .into(ivHeader)*/
            ivHeader.loadTintedIcon(mPublicPlace!!)
        }

        if(SharedPreferencesManager().getFavourites(this) != null){
            for (place in SharedPreferencesManager().getFavourites(this)!!){
                if(place.placeId == mPublicPlace!!.placeId){
                    cbFav.isChecked = true
                    placeIsFaved = true
                }else{
                    cbFav.isChecked = false
                    placeIsFaved = false
                }
            }
        }

        cbFav.setOnCheckedChangeListener { buttonView, isChecked ->
            var favourites = if(SharedPreferencesManager().getFavourites(this@PlaceDetailActivity) != null)
                SharedPreferencesManager().getFavourites(this@PlaceDetailActivity) else mutableListOf()
            if(!placeIsFaved){
                favourites!!.add(mPublicPlace!!)
                placeIsFaved = true
            }else{
                favourites!!.remove(mPublicPlace!!)
                placeIsFaved = false
            }
            SharedPreferencesManager().setFavourites(this@PlaceDetailActivity, favourites)
        }

        setComments()
    }

    override fun onResume() {
        super.onResume()
        mPresenter!!.getPlaceAvailability(mPublicPlace!!.placeId!!)
    }

    //TODO AVAILABILITY GLOBAL - edit with API results
    override fun setAvailabilityGraphic(availabilityList: List<PieChartItem>) {
        if (availabilityList.isEmpty()) {
            displayMessage("No se pudieron obtener los datos del lugar, por favor intentalo nuevamente")
        } else {
            var list = availabilityList as MutableList<PieChartItem>
            pieChartPagerAdapter = HistoryInformationAdapter(list.asReversed())
            vpPieChart.adapter = pieChartPagerAdapter
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    //TODO edit
    private fun setComments() {
        var list: MutableList<Comment> = mutableListOf()
        list.add(Comment(User("Marta", "Gómez", person1), 5f, "Este lugar no estaba muy lleno", "20/9"))
        list.add(Comment(User("Juan", "Gómez", person2), 3f, "Este lugar no estaba muy lleno", "20/9"))
        list.add(Comment(User("Martin", "Gómez", person3), 4f, "Este lugar no estaba muy lleno", "20/9"))
        list.add(Comment(User("Luis", "Gómez", person4), 2f, "Este lugar no estaba muy lleno", "20/9"))
        list.add(Comment(User("Lucia", "Gómez", person5), 5f, "Este lugar no estaba muy lleno", "20/9"))
        list.add(Comment(User("Jimena", "Gómez", person6), 4f, "Este lugar no estaba muy lleno", "20/9"))

        var adapter = CommentsAdapter(list)
        rvComments.adapter = adapter
        rvComments.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

    }

    private fun setPlaceInformation(mPublicPlace: PublicPlace) {
        /*GlideApp.with(this)
            .load(mPublicPlace.icon)
            .into(ivPlaceIcon)*/
        ivPlaceIcon.loadTintedIcon(mPublicPlace)

        tvPlaceName.text = mPublicPlace.name
        rbStars.rating = if (mPublicPlace.rating != null) mPublicPlace.rating!!.toFloat() else 0f
        tvPlaceRating.text = if (mPublicPlace.rating != null)
            mPublicPlace.rating!!.toBigDecimal().setScale(1, RoundingMode.UP).toString() else "0.0"
        tvPlaceAddress.text = mPublicPlace.vicinity

        //TODO AVAILABILITY - add API results
        /*tvAvailabilityHumanQuantity.text = mPublicPlaceCurrentAvailability!!.PromedioIA.toString() + "%"
        tvAvailabilityAIQuantity.text = mPublicPlaceCurrentAvailability!!.PromedioEncuesta.toString() + "%"*/

        if(mPublicPlaceCurrentAvailability!!.PromedioIA == null
            && mPublicPlaceCurrentAvailability!!.PromedioEncuesta == null){
            llContainerAvailability.visibility = View.GONE
            llContainerNoAvailability.visibility = View.VISIBLE

            btnAddReport.setOnClickListener {
                val intent = Intent(this@PlaceDetailActivity, ReportFromPlaceActivity::class.java)
                intent.putExtra(AppConstants.PUBLIC_PLACE, mPublicPlace)
                startActivity(intent)
            }
        }else {
            llContainerNoAvailability.visibility = View.GONE
            llContainerAvailability.visibility = View.VISIBLE

            when (mPublicPlaceCurrentAvailability!!.PromedioIA){
                Availability.LOW.value.toUpperCase() -> {
                    tvAvailabilityHuman.background = resources.getDrawable(R.drawable.bg_green_rounded, null)
                }
                Availability.MEDIUM.value.toUpperCase() -> {
                    tvAvailabilityHuman.background = resources.getDrawable(R.drawable.bg_orange_rounded, null)
                }
                Availability.HIGH.value.toUpperCase() -> {
                    tvAvailabilityHuman.background = resources.getDrawable(R.drawable.bg_red_rounded, null)
                }
            }
            tvAvailabilityAI.text = mPublicPlaceCurrentAvailability!!.PromedioIA

            when (mPublicPlaceCurrentAvailability!!.PromedioEncuesta){
                Availability.LOW.value.toUpperCase() -> {
                    tvAvailabilityHuman.background = resources.getDrawable(R.drawable.bg_green_rounded, null)
                }
                Availability.MEDIUM.value.toUpperCase() -> {
                    tvAvailabilityHuman.background = resources.getDrawable(R.drawable.bg_orange_rounded, null)
                }
                Availability.HIGH.value.toUpperCase() -> {
                    tvAvailabilityHuman.background = resources.getDrawable(R.drawable.bg_red_rounded, null)
                }
            }
            tvAvailabilityHuman.text = mPublicPlaceCurrentAvailability!!.PromedioEncuesta

            btnGoToReport.setOnClickListener {
                val intent = Intent(this@PlaceDetailActivity, ReportFromPlaceActivity::class.java)
                intent.putExtra(AppConstants.PUBLIC_PLACE, mPublicPlace)
                startActivity(intent)
            }
        }

        //TODO AVAILABILITY - add API results
        mPresenter!!.getPlaceGlobalAvailability(mPublicPlace.placeId!!)

        mPresenter!!.getPromotions(mPublicPlace.id!!)

    }

    override fun getContext(): Context {
        return this
    }

    override fun displayMessage(message: String) {
        DisplayMessage().displayMessage(message, clContainer)
    }

    override fun startPresenter() {
        mPresenter = PlaceDetailActivityPresenter()
        mPresenter!!.startView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun displayPromotions(promotions: List<Promotion>) {
        if (promotions.isEmpty()) {
            llPromotionsContainer.visibility = View.GONE
        } else {
            if (mPromotionsAdapter == null) {
                mPromotionsAdapter = PromotionsAdapter(promotions)
                rvPromotions.adapter = mPromotionsAdapter
                rvPromotions.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            } else {
                mPromotionsAdapter!!.updatePromotionsList(promotions)
            }
            llPromotionsContainer.visibility = View.VISIBLE
        }
    }

    override fun setAvailability(availability: PollAverageResponse) {
        /*tvAvailabilityHumanQuantity.text = "10%"
        tvAvailabilityAIQuantity.text = "60%"*/
        when (mPublicPlaceCurrentAvailability!!.PromedioEncuesta){
            Availability.LOW.value.toUpperCase() -> {
                tvAvailabilityHuman.background = resources.getDrawable(R.drawable.bg_green_rounded, null)
            }
            Availability.MEDIUM.value.toUpperCase() -> {
                tvAvailabilityHuman.background = resources.getDrawable(R.drawable.bg_orange_rounded, null)
            }
            Availability.HIGH.value.toUpperCase() -> {
                tvAvailabilityHuman.background = resources.getDrawable(R.drawable.bg_red_rounded, null)
            }
        }
        tvAvailabilityHuman.text = availability.PromedioEncuesta

        when (mPublicPlaceCurrentAvailability!!.PromedioIA){
            Availability.LOW.value.toUpperCase() -> {
                tvAvailabilityAI.background = resources.getDrawable(R.drawable.bg_green_rounded, null)
            }
            Availability.MEDIUM.value.toUpperCase() -> {
                tvAvailabilityAI.background = resources.getDrawable(R.drawable.bg_orange_rounded, null)
            }
            Availability.HIGH.value.toUpperCase() -> {
                tvAvailabilityAI.background = resources.getDrawable(R.drawable.bg_red_rounded, null)
            }
        }
        tvAvailabilityAI.text = availability.PromedioIA
    }

}