package com.roulettegame.roulettegameapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private var PRIVATE_MODE = 0
    private val PREF_INDEX = "index"
    private val PREF_FIRST_RUN = "first_run"
    private val PREF_GIFT = "gift"
    private lateinit var mRandom: Random
    private var mDegree : Float = 0f
    private var mDegreeOld : Float = 0f
    private var tempDegree : Float = 0f
    private var valueOfSum: String = ""
    //private val caseOfGift: IntArray = intArrayOf(0, 0, 1, 4, 0, 0, 0, 0, 3, 3, 0, 3, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 5, 0, 0, 4, 0, 5, 0, 0, 3, 0, 4, 0, 3, 5, 0, 0, 0, 0, 0, 0, 1, 1, 0, 6, 2, 0, 2, 0, 0, 0, 1, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 4, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 3, 0, 0, 4, 0, 0, 0, 0, 4, 1, 1, 0, 0, 0, 0, 1, 0, 0, 2, 0, 0, 6, 4, 1, 0, 0, 0, 4, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 5, 3, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 5, 0)
    private var caseOfGift = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPref: SharedPreferences = getSharedPreferences(PREF_INDEX, PRIVATE_MODE)
        // check initial mode
        val isFirstRun = sharedPref.getBoolean(PREF_FIRST_RUN, true)
        if(isFirstRun){
            val editor = sharedPref.edit()
            editor.putString(PREF_GIFT, generateArray())
            editor.putBoolean(PREF_FIRST_RUN,false)
            editor.apply()
        }
        caseOfGift = sharedPref.getString(PREF_GIFT,"").toString()
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
            when(caseOfGift[sharedPref.getInt(PREF_INDEX, 0)]){
                '0' -> tempDegree = 18f
                '1' -> tempDegree = 342f
                '2' -> tempDegree = 306f
                '3' -> tempDegree = 270f
                '4' -> tempDegree = 198f
                '5' -> tempDegree = 126f
                '6' -> tempDegree = 54f
            }
            mDegree = (360-tempDegree + 2160)
            val mRotate = RotateAnimation(
                mDegreeOld, mDegree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f)
            //time to rotate
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
                    tvGiftName.text = getGift(360- mDegree % 360)
                    val editor = sharedPref.edit()
                    val index = sharedPref.getInt(PREF_INDEX, 0) + 1
                    editor.putInt(PREF_INDEX, index)
                    editor.apply()
                    //tvGiftName.text = currentNumber(360-mDegree % 360)
                    btn_spin.isClickable = true
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
            imRoulette.startAnimation(mRotate)
        }

        //clear pref
        imPointer.setOnLongClickListener{
            val editor = sharedPref.edit()
            editor.putInt(PREF_INDEX, 0)
            editor.apply()
            Toast.makeText(this, sharedPref.getInt(PREF_INDEX, -1).toString(), Toast.LENGTH_SHORT).show()
            true
        }

        //show/hidden edit text
        btn_change_value.setOnLongClickListener{
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
            true
        }
        //save value of sum
        //show/hidden edit text
    }

   private fun generateArray(): String{
        val arr = mutableListOf<Int>()
        for(i in 1..110){
            arr.add(0)
        }
        for(i in 1..10)
            arr.add(1)
        for(i in 1..5)
            arr.add(2)
        for(i in 1..10)
            arr.add(3)
        for(i in 1..10)
            arr.add(4)
        for(i in 1..5)
            arr.add(5)
        for(i in 1..2)
            arr.add(6)
        arr.shuffle()
        var result = ""
        for(i in 0 until arr.size)
            result+= arr[i]
        return result
    }

    fun getGift(degrees: Float) : String{
        Log.d("uytai", degrees.toString())
        var text = ""
        //no gift
        if((degrees in 0.0..36.0)
            || (degrees > 72 && degrees <= 108)
            || (degrees > 144 && degrees <= 180)
            || (degrees > 216 && degrees <= 252)){
            text = "Trật rồi! Tiếc quá!"
            imGift.setImageResource(R.drawable.cbmmls)
            Log.d("uytai", "MM")
        }
        //card 50
        if(degrees > 36 && degrees <=72){
            text = "WAOO!! Thẻ cào 50K"
            imGift.setImageResource(R.drawable.card50)
            Log.d("uytai", "50")
        }
        //card 20
        if(degrees > 108 && degrees <=144){
            text = "Thẻ cào 20K"
            imGift.setImageResource(R.drawable.card20)
            Log.d("uytai", "20")
        }
        //card 10
        if(degrees > 180 && degrees <=216){
            text = "Thẻ cào 10K"
            imGift.setImageResource(R.drawable.card10)
            Log.d("uytai", "10")
        }
        //strongbow
        if(degrees > 252 && degrees <=288){
            text = "1 ly Strongbow nhé!"
            imGift.setImageResource(R.drawable.sb)
            Log.d("uytai", "SB")
        }
        //2BT
        if(degrees > 288 && degrees <=324){
            text = "2 bịch bánh tráng luôn!"
            imGift.setImageResource(R.drawable.bt2)
            Log.d("uytai", "2BT")
        }
        //1BT
        if(degrees > 324 && degrees <=360){
            text = "1 bịch bánh tráng"
            imGift.setImageResource(R.drawable.bt1)
            Log.d("uytai", "1BT")
        }
        return text
    }
}
