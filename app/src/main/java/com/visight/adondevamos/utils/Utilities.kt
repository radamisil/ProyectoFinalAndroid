package com.visight.adondevamos.utils

import android.util.Log
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

open class Utilities {

    fun loadPlaceImageFromGoogle(photoReference: String) : String {
        return "https://maps.googleapis.com/maps/api/place/" +
                "photo?maxwidth=500&photoreference=" +
                photoReference +
                "&key=AIzaSyBMfHFvJTPHMgD5zBbRbuJdOjIOJ_HdL4o"
    }

    fun getDayFromDate(dateReceived: String): String{
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("EEEE")
        val output = formatter.format(parser.parse(dateReceived))
        Log.d("DATE", output)
        return output.capitalize()
    }
}