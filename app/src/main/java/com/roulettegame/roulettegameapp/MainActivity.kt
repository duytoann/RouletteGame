package com.roulettegame.roulettegameapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.*
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private var PRIVATE_MODE = 0
    private val PREF_INDEX = "index"
    private val PREF_FIRST_RUN = "first_run"
    private val PREF_GIFT = "gift"
    private lateinit var mRandom: Random
    private var mDegree: Float = 0f
    private var mDegreeOld: Float = 0f
    private var tempDegree: Float = 0f
//    private var initDegree: Float = 18f
    private var initDegree: Float = 0f
    private var valueOfSum: String = ""
    private var index = 0
    private var caseOfGift = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //hidden status bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        //
        val sharedPref: SharedPreferences = getSharedPreferences(PREF_INDEX, PRIVATE_MODE)
        index = sharedPref.getInt(PREF_INDEX, 0)
        // check initial mode
        val isFirstRun = sharedPref.getBoolean(PREF_FIRST_RUN, true)
        if (isFirstRun) {
            val editor = sharedPref.edit()
            editor.putString(PREF_GIFT, generateArray())
            editor.putBoolean(PREF_FIRST_RUN, false)
            editor.apply()
        }
        caseOfGift = sharedPref.getString(PREF_GIFT, "").toString()
        mRandom = Random()
        btn_spin.setOnClickListener {
            //check out of gift
            if (index >= caseOfGift.length) {
                Toast.makeText(
                    this,
                    "Đã hết phần thưởng, vui lòng liên hệ ban tổ chức",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                //running
                //disible button
                btn_spin.isClickable = false
                btn_spin.setImageResource(R.drawable.button)
                //hidden gift image
                imGift.visibility = View.GONE
                gim_congras.visibility = View.GONE
                //show watting gif
                gimWaiting.visibility = View.VISIBLE
                tvGiftName.text = ""
                //
                mDegreeOld = mDegree % 360
                //speed of rotation
                when (caseOfGift[index]) {
                    '0' -> tempDegree = 0f
                    '1' -> tempDegree = 45f
                    '2' -> tempDegree = 90f
                    '3' -> tempDegree = 135f
                    '4' -> tempDegree = 180f
                    '5' -> tempDegree = 225f
                    '6' -> tempDegree = 270f
                    '7' -> tempDegree = 315f
                }
                mDegree = (360 - (tempDegree - initDegree) + 2160)
                val mRotate = RotateAnimation(
                    mDegreeOld, mDegree,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f
                )
                //time to rotate
                mRotate.duration = 5000
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
                        tvGiftName.text = getGift2(360 - mDegree % 360)
                        val editor = sharedPref.edit()
                        index = sharedPref.getInt(PREF_INDEX, 0) + 1
                        editor.putInt(PREF_INDEX, index)
                        editor.apply()
                        //tvGiftName.text = currentNumber(360-mDegree % 360)
                        btn_spin.isClickable = true
                        btn_spin.setImageResource(R.drawable.button2)
                    }

                    override fun onAnimationRepeat(animation: Animation) {}
                })
                imRoulette.startAnimation(mRotate)
            }
        }

        var count = 0
        imPointer.setOnClickListener {
            count++
            Handler().postDelayed({
                if (count < 5) {
                    count = 0
                }
            }, 2000)
            if (count == 5) {
                val editor = sharedPref.edit()
                for (i in index until caseOfGift.length) {
                    if (caseOfGift[i] != '0') {
                        index = i
                        editor.putInt(PREF_INDEX, index)
                        editor.apply()
                        break
                    }
                }
                val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                // Vibrate for 500 milliseconds
                // Vibrate for 500 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(400, VibrationEffect.DEFAULT_AMPLITUDE))
                } else { //deprecated in API 26
                    v.vibrate(400)
                }
                count = 0
            }
        }

        var count2 = 0
        btn_change_value.setOnClickListener {
            count2++
            Handler().postDelayed({
                if (count2 < 5) {
                    count2 = 0
                }
            }, 2000)
            if (count2 == 5) {
                val intent = Intent(this, AdminActivity2::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
                count2 = 0
            }
        }

        //Get Firebase Instance
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
//        database.goOffline()

        //todo ok
        var empReference2: DatabaseReference = database.getReference("employee")

        //todo ok
        val query = empReference2.orderByChild("genid").equalTo("10592215")
        query.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val id = snapshot.key

                val emp = snapshot.getValue(Employee::class.java)
                Log.d("TOAN22222", emp.toString() + "\n id: $id")
                //sau khi quay
                emp?.status = "1"
                empReference2.child(id!!).setValue(emp)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        Log.d("TOAN", "BEGIN")
        empReference2.addChildEventListener(childEventListener)
    }

    private val childEventListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            TODO("Not yet implemented")
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            TODO("Not yet implemented")
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d("TOAN", "3")
            TODO("Not yet implemented")
        }
    }

    override fun onResume() {
        super.onResume()
        val sharedPref: SharedPreferences = getSharedPreferences(PREF_INDEX, PRIVATE_MODE)
        caseOfGift = sharedPref.getString(PREF_GIFT, "").toString()
    }

    private fun generateArray(): String {
        val arr = mutableListOf<Int>()
        // tổng số lượt quay
        for (i in 1..872) {
            arr.add(0)
        }

        for (i in 1..70)
            arr.add(1)
        for (i in 1..60)
            arr.add(2)
        for (i in 1..10)
            arr.add(3)
        for (i in 1..70)
            arr.add(4)
        for (i in 1..50)
            arr.add(5)
        for (i in 1..60)
            arr.add(6)
        arr.shuffle()
        var result = ""
        for (i in 0 until arr.size)
            result += arr[i]
        return result
    }

    fun getGift(degrees: Float): String {
        Log.d("uytai", degrees.toString())
        var text = ""
        //no gift
        if ((degrees in 0.0..36.0)
            || (degrees > 72 && degrees <= 108)
            || (degrees > 144 && degrees <= 180)
            || (degrees > 216 && degrees <= 252)
        ) {
            text = "Trật rồi! Tiếc quá!"
            imGift.setImageResource(R.drawable.mm)
            Log.d("uytai", "MM")
        }
        //card 50
        if (degrees > 36 && degrees <= 72) {
            text = "WAOO!! Thẻ cào 50K"
            imGift.setImageResource(R.drawable.the_50k)
            Log.d("uytai", "50")
        }
        //card 20
        if (degrees > 108 && degrees <= 144) {
            text = "Thẻ cào 20K"
            imGift.setImageResource(R.drawable.the_20k)
            Log.d("uytai", "20")
        }
        //card 10
        if (degrees > 180 && degrees <= 216) {
            text = "Thẻ cào 10K"
            imGift.setImageResource(R.drawable.the_10k)
            Log.d("uytai", "10")
        }
        //strongbow
        if (degrees > 252 && degrees <= 288) {
            text = "1 ly Strongbow nhé!"
            imGift.setImageResource(R.drawable.strongbow)
            Log.d("uytai", "SB")
        }
        //2BT
        if (degrees > 288 && degrees <= 324) {
            text = "2 bịch bánh tráng luôn!"
            imGift.setImageResource(R.drawable.banh_trang_2)
            Log.d("uytai", "2BT")
        }
        //1BT
        if (degrees > 324 && degrees <= 360) {
            text = "1 bịch bánh tráng"
            imGift.setImageResource(R.drawable.banh_trang_1)
            Log.d("uytai", "1BT")
        }
        return text
    }

    fun getGift2(degrees: Float): String {
        Log.d("uytai", degrees.toString())
        var text = ""
        //no gift
        if (degrees == 0f || degrees == 360f) {
            text = "Chúc bạn may mắn lần sau!"
            imGift.setImageResource(R.drawable.mm)
            gim_congras.visibility = View.GONE
            Log.d("uytai", "MM")
        } else {
            gim_congras.visibility = View.VISIBLE
        }
        if (degrees == 45f) {
            text = "Bộ cắt móng"
            imGift.setImageResource(R.drawable.bocatmong)
            Log.d("uytai", "cắt móng")
        }
        if (degrees == 90f) {
            text = "Dao cạo chân mày"
            imGift.setImageResource(R.drawable.daocao)
            Log.d("uytai", "dao cạo chân mày")
        }
        if (degrees == 135f) {
            text = "Thú bông!"
            imGift.setImageResource(R.drawable.chuoi)
            Log.d("uytai", "Chuối")
        }
        if (degrees == 180f) {
            text = "Bông tẩy trang"
            imGift.setImageResource(R.drawable.bongtaytrang)
            Log.d("uytai", "Bông tẩy trang")
        }
        if (degrees == 225f) {
            text = "Băng cài tóc"
            imGift.setImageResource(R.drawable.caitoc)
            Log.d("uytai", "Cài tóc")
        }
        if (degrees == 270f) {
            text = "Bộ dưỡng môi Vaserlin"
            imGift.setImageResource(R.drawable.vaserlin)
            Log.d("uytai", "Varserlin")
        }
        if (degrees == 315f) {
            text = "Túi đựng mỹ phẩm"
            imGift.setImageResource(R.drawable.tuidung)
            Log.d("uytai", "Túi đựng")
        }
        return text
    }

}
