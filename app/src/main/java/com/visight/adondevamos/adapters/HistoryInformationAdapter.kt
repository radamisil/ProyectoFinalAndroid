package com.visight.adondevamos.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.github.mikephil.charting.charts.PieChart
import com.visight.adondevamos.R
import com.visight.adondevamos.data.entity.PieChartItem
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.animation.Easing
import android.graphics.Color
import android.os.Build
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.visight.adondevamos.utils.Utilities


class HistoryInformationAdapter(var items: MutableList<PieChartItem>) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var v = LayoutInflater.from(container.context)
            .inflate(R.layout.item_pie_chart, container, false)

        var pieChart = v.findViewById<PieChart>(R.id.pieChart)
        var tvDay = v.findViewById<TextView>(R.id.tvDay)

        tvDay.text = Utilities().getDayFromDate(items[position].fecha!!)
        setPieChart(pieChart, position)

        container.addView(v)
        return v
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int = items.size

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        //super.destroyItem(container, position, `object`)
    }

    private fun setPieChart(chart: PieChart, position: Int) {
        chart.setUsePercentValues(true)
        chart.getDescription().setEnabled(false)
        chart.setExtraOffsets(5f, 10f, 5f, 5f)

        chart.setDragDecelerationFrictionCoef(0.95f)

        chart.setDrawHoleEnabled(true)
        chart.setHoleColor(Color.WHITE)

        chart.setTransparentCircleColor(Color.WHITE)
        chart.setTransparentCircleAlpha(110)

        chart.setHoleRadius(10f)
        chart.setTransparentCircleRadius(15f)

        chart.setDrawCenterText(true)

        chart.setRotationAngle(0f)
        // enable rotation of the chart by touch
        chart.setRotationEnabled(false)
        chart.setHighlightPerTapEnabled(true)

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        //chart.setOnChartValueSelectedListener(this)

        chart.animateY(1400, Easing.EaseInOutQuad)
        // chart.spin(2000, 0, 360);

        val l = chart.getLegend()
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
        l.setOrientation(Legend.LegendOrientation.VERTICAL)
        l.setDrawInside(false)
        l.setXEntrySpace(7f)
        l.setYEntrySpace(0f)
        l.setYOffset(0f)

        // entry label styling
        chart.setEntryLabelColor(Color.WHITE)
        //chart.setEntryLabelTypeface(tfRegular)
        chart.setEntryLabelTextSize(12f)

        //setData(12, 5f, chart)
        setData(items.size, 5f, chart, position)
    }

    private fun setData(count: Int, range: Float, pieChart: PieChart, position: Int) {
        val entries = arrayListOf<PieEntry>()

        entries.add(
            PieEntry(
                items[position].bajaporcent!!.toFloat(),
                "Bajo",
                pieChart.context.resources.getDrawable(R.drawable.ic_fav, null)
            )
        )
        entries.add(
            PieEntry(
                items[position].mediaporcent!!.toFloat(),
                "Medio",
                pieChart.context.resources.getDrawable(R.drawable.ic_fav, null)
            )
        )
        entries.add(
            PieEntry(
                items[position].altaporcent!!.toFloat(),
                "Alto",
                pieChart.context.resources.getDrawable(R.drawable.ic_fav, null)
            )
        )

        val dataSet = PieDataSet(entries, "")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        val colors = arrayListOf<Int>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            colors.add(pieChart.context.resources.getColor(R.color.colorGreen, null))
            colors.add(pieChart.context.resources.getColor(R.color.colorAccent, null))
            colors.add(pieChart.context.resources.getColor(R.color.colorRed, null))
        }

        dataSet.setColors(colors)
        //dataSet.setSelectionShift(0f);

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(pieChart))
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        //data.setValueTypeface(tfLight)
        pieChart.setData(data)

        // undo all highlights
        pieChart.highlightValues(null)

        pieChart.invalidate()
    }
}