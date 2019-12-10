package com.visight.adondevamos.utils

import com.github.mikephil.charting.formatter.ValueFormatter

class CustomValueFormatter(var labels: Array<String>) : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return labels[value.toInt()-1]
    }
}