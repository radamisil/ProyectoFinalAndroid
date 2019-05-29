package com.visight.adondevamos.ui.start

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Pair
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*
import kotlin.concurrent.schedule
import android.os.Handler
import com.visight.adondevamos.R
import com.visight.adondevamos.ui.main.MainActivity


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //transparent status bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        val animatorLogo = AnimatorInflater.loadAnimator(this@SplashActivity, R.animator.animator_fade_in)
        animatorLogo.duration = 650
        animatorLogo.setTarget(ivLogo)
        animatorLogo.start()

        //TODO PREGUNTAR OTRA VEZ POR QUÉ USA OBJECT Y NO UNA VARIABLE CUALQUIERA
        animatorLogo.addListener(object : Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationEnd(animation: Animator?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                Handler().postDelayed({
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }, 150)
            }

            override fun onAnimationCancel(animation: Animator?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                finish()
                onBackPressed()
            }

            override fun onAnimationStart(animation: Animator?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
}