package com.visight.adondevamos.utils

import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar



open class DisplayMessage {

    open fun displayMessage(message: String, container: View) {
        val snackbar = Snackbar.make(container, message, Snackbar.LENGTH_SHORT)
        snackbar.view.setBackgroundColor(Color.parseColor("#880E4F"))
        snackbar.show()
    }
}