package com.roulettegame.roulettegameapp

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mRandom: Random
    private var mDegree : Float = 0f
    private var mDegreeOld : Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mRandom = Random()
        btn_spin.setOnClickListener{
            //running
            //disible button
            btn_spin.isClickable = false
            //hidden gift image
            imGift.visibility = View.GONE
            //show watting gif
            gimWaiting.visibility = View.VISIBLE
            tvGiftName.text = ""
            //
            mDegreeOld = mDegree%360
            //speed of rotation
            mDegree = (1000).toFloat()
            var mRotate = RotateAnimation(mDegreeOld, mDegree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f)
            mRotate.duration = 3600
            mRotate.fillAfter = true
            mRotate.interpolator = DecelerateInterpolator()
            mRotate.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    tvGiftName.text = ""
                }

                override fun onAnimationEnd(animation: Animation) {
                    //stop
                    gimWaiting.visibility = View.GONE
                    imGift.visibility = View.VISIBLE
                    tvGiftName.text = currentNumber(360 - mDegree % 360)
                    btn_spin.isClickable = true
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
            imRoulette.startAnimation(mRotate)
        }
    }

    //
    fun currentNumber( degrees: Float): String {
        var text = ""
        if(degrees<=45){
            text = "Red"
            imGift.setImageResource(R.drawable.red)
        }
        if(degrees > 45 && degrees <=90){
            imGift.setImageResource(R.drawable.orange)
            text = "Orange"
        }
        if(degrees > 90 && degrees <=135){
            imGift.setImageResource(R.drawable.yellow)
            text = "Yellow"
        }
        if(degrees > 135 && degrees <=180){
            imGift.setImageResource(R.drawable.green)
            text = "Green"
        }
        if(degrees > 180 && degrees <=225){
            imGift.setImageResource(R.drawable.blue)
            text = "Blue"
        }
        if(degrees > 225 && degrees <=270){
            imGift.setImageResource(R.drawable.turquoise)
            text = "Turquoise"
        }
        if(degrees > 270 && degrees <=315){
            imGift.setImageResource(R.drawable.purple)
            text = "Purple"
        }
        if(degrees > 315){
            imGift.setImageResource(R.drawable.pink)
            text = "Pink"
        }
        return text
    }
}
