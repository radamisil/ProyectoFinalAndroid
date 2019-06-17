package com.visight.adondevamos.ui.base

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

open abstract class BaseActivity: AppCompatActivity() {
    abstract  fun getContext(): Context
}