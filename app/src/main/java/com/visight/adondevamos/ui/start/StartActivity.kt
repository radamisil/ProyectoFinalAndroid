package com.visight.adondevamos.ui.start

import android.animation.Animator
import android.animation.AnimatorInflater
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.visight.adondevamos.R
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        //transparent status bar on android Kitkat onwards
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window // in Activity's onCreate() for instance
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        val animatorSubtitle = AnimatorInflater.loadAnimator(this@StartActivity, R.animator.animator_fade_in)
        animatorSubtitle.setTarget(tvSubtitle)
        animatorSubtitle.startDelay = 100
        animatorSubtitle.start()

        animatorSubtitle.addListener(object: Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationEnd(animation: Animator?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                val animatorLogin = AnimatorInflater.loadAnimator(this@StartActivity, R.animator.animator_fade_in)
                animatorLogin.setTarget(btnLogin)
                animatorLogin.start()

                animatorLogin.addListener(object: Animator.AnimatorListener{
                    override fun onAnimationRepeat(animation: Animator?) {
                        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        val animatorRegister = AnimatorInflater.loadAnimator(this@StartActivity, R.animator.animator_fade_in)
                        animatorRegister.setTarget(btnRegister)
                        animatorRegister.start()
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onAnimationStart(animation: Animator?) {
                        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                })
            }

            override fun onAnimationCancel(animation: Animator?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationStart(animation: Animator?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    //TODO PREGUNTAR --> POR QUE HACE FALTA OTRO FINISH() ACA?
    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}