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
import com.visight.adondevamos.data.entity.Comment
import com.visight.adondevamos.data.entity.PublicPlace
import com.visight.adondevamos.data.entity.User
import com.visight.adondevamos.ui.base.BaseActivity
import com.visight.adondevamos.ui.main.place.report.ReportFromPlaceActivity
import com.visight.adondevamos.utils.AppConstants
import com.visight.adondevamos.utils.GlideApp
import com.visight.adondevamos.utils.loadImage
import kotlinx.android.synthetic.main.activity_place_detail.*
import kotlinx.android.synthetic.main.layout_place_detail_content.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.math.RoundingMode
import android.R.attr.data
import com.github.mikephil.charting.charts.LineChart
import android.graphics.Color
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.data.LineDataSet


class PlaceDetailActivity : BaseActivity(), PlaceDetailActivityContract.View{
    var mPresenter: PlaceDetailActivityContract.Presenter? = null
    var mPublicPlace: PublicPlace? = null

    //profile images
    var person1 = "https://www.google.com/imgres?imgurl=https%3A%2F%2Ffsmedia.imgix.net%2Fca%2Fae%2F25%2F9c%2Fb6f8%2F476e%2Fb006%2F0afd6ca06e33%2Fscreen-shot-2019-02-13-at-25638-pmpng.png%3Fauto%3Dcompress%26h%3D675%26w%3D1200%26crop%3Dedges%26fit%3Dcrop&imgrefurl=https%3A%2F%2Fwww.inverse.com%2Farticle%2F53280-this-person-does-not-exist-gans-website&docid=8lhLZgIzKWeTaM&tbnid=nYH9CDYE3y2IoM%3A&vet=10ahUKEwjajL_RhZDlAhXGJbkGHS66CbUQMwhMKAIwAg..i&w=1200&h=675&bih=638&biw=1345&q=person%20face&ved=0ahUKEwjajL_RhZDlAhXGJbkGHS66CbUQMwhMKAIwAg&iact=mrc&uact=8"
    var person2 = "https://www.pexels.com/photo/face-facial-hair-fine-looking-guy-614810/"
    var person3 = "https://unsplash.com/s/photos/man-face"
    var person4 = "https://pixabay.com/photos/face-the-person-men-s-1389832/"
    var person5 = "http://www.whichfaceisreal.com/fakeimages/image-2019-02-17_020449.jpeg"
    var person6 = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS6-_swo2nPuVu92bxYEXENm3l-NaPUrfLoAN0y0YH2FQSSkZTuyA"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)
        startPresenter()

        mPublicPlace = intent.getParcelableExtra(AppConstants.PUBLIC_PLACE)
        setUpToolbar(toolbar, mPublicPlace!!.name, ivLogo)
        setPlaceInformation(mPublicPlace!!)

        if(mPublicPlace!!.photos != null && mPublicPlace!!.photos!!.isNotEmpty()){
            ivHeader.loadImage(this, mPublicPlace!!, progressBarPlaceDetailImage)
        }else{
            progressBarPlaceDetailImage.visibility = View.GONE
            GlideApp.with(this)
                .load(mPublicPlace!!.icon)
                .into(ivHeader)
        }

        chart.setViewPortOffsets(0f, 0f, 0f, 0f)
        chart.setBackgroundColor(Color.rgb(104, 241, 175))

        // no description text
        chart.getDescription().setEnabled(false)

        // enable touch gestures
        chart.setTouchEnabled(true)

        // enable scaling and dragging
        chart.setDragEnabled(true)
        chart.setScaleEnabled(true)

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false)

        chart.setDrawGridBackground(false)
        chart.setMaxHighlightDistance(300f)

        var x = chart.getXAxis()
        x.setEnabled(false)

        var y = chart.getAxisLeft()
        y.setLabelCount(6, false)
        y.setTextColor(Color.WHITE)
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        y.setDrawGridLines(false)
        y.setAxisLineColor(Color.WHITE)

        chart.getAxisRight().setEnabled(false)

        chart.getLegend().setEnabled(false)

        chart.animateXY(2000, 2000)

        // don't forget to refresh the drawing
        chart.invalidate()

        setData(100, 200f)

        btnGoToReport.setOnClickListener {
            val intent = Intent(this@PlaceDetailActivity, ReportFromPlaceActivity::class.java)
            intent.putExtra(AppConstants.PUBLIC_PLACE, mPublicPlace)
            startActivity(intent)
        }

        setComments()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == android.R.id.home){
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
        GlideApp.with(this)
            .load(mPublicPlace.icon)
            .into(ivPlaceIcon)

        tvPlaceName.text = mPublicPlace.name
        rbStars.rating = if(mPublicPlace.rating != null) mPublicPlace.rating!!.toFloat() else 0f
        tvPlaceRating.text = if(mPublicPlace.rating != null)
            mPublicPlace.rating!!.toBigDecimal().setScale(1, RoundingMode.UP).toString() else "0.0"
        tvPlaceAddress.text = mPublicPlace.vicinity
        tvAvailabilityHuman.text = "Baja"
        tvAvailabilityAI.text = "Media"
    }

    override fun getContext(): Context {
        return this
    }

    override fun displayMessage(message: String) {

    }

    override fun startPresenter() {
        mPresenter = PlaceDetailActivityPresenter()
        mPresenter!!.startView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setData(count: Int, range: Float) {

        val values : MutableList<Entry> = mutableListOf()

        for (i in 0 until count) {
            val `val` = (Math.random() * (range + 1)).toFloat() + 20
            values.add(Entry(i.toFloat(), `val`))
        }

        val set1: LineDataSet

        if (chart.data != null && chart.data.dataSetCount > 0) {
            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values, "DataSet 1")

            set1.mode = LineDataSet.Mode.CUBIC_BEZIER
            set1.cubicIntensity = 0.2f
            set1.setDrawFilled(true)
            set1.setDrawCircles(false)
            set1.lineWidth = 1.8f
            set1.circleRadius = 4f
            set1.setCircleColor(Color.WHITE)
            set1.highLightColor = Color.rgb(244, 117, 117)
            set1.color = Color.WHITE
            set1.fillColor = Color.WHITE
            set1.fillAlpha = 100
            set1.setDrawHorizontalHighlightIndicator(false)
            set1.fillFormatter = IFillFormatter { dataSet, dataProvider -> chart.axisLeft.axisMinimum }

            // create a data object with the data sets
            val data = LineData(set1)
            data.setValueTextSize(9f)
            data.setDrawValues(false)

            // set data
            chart.data = data
        }
    }

}