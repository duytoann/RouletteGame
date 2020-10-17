package com.roulettegame.roulettegameapp

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.inputmethodservice.Keyboard
import android.opengl.Visibility
import android.os.*
import android.util.Log
import android.view.Gravity
import android.view.KeyboardShortcutInfo
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.eemployee_dialog.*
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
    private var initDegree: Float = 0f
    private var valueOfSum: String = ""
    private var index = 0
    private var caseOfGift = ""
    private var employee: Employee = Employee()


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
            show_greeting_quote.visibility = View.GONE
            btn_spin.visibility = View.GONE
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
                    '1' -> tempDegree = 36f
                    '2' -> tempDegree = 72f
                    '3' -> tempDegree = 108f
                    '4' -> tempDegree = 144f
                    '5' -> tempDegree = 180f
                    '6' -> tempDegree = 216f
                    '7' -> tempDegree = 252f
                    '8' -> tempDegree = 288f
                    '9' -> tempDegree = 324f
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
        btn_add_gen_id.setOnClickListener {
            show_greeting_quote.visibility = View.GONE
            tvGiftName.visibility = View.GONE
            imGift.visibility = View.GONE
            gim_congras.visibility = View.GONE
            count2++
            Handler().postDelayed({
                if (count2 < 5) {
                    count2 = 0
                }
            }, 2000)
            if (count2 == 2) {

                var dialog = Dialog(this)
                dialog.setContentView(R.layout.eemployee_dialog)
                dialog.setCanceledOnTouchOutside(false)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()

                var btnCancel = dialog.findViewById<Button>(R.id.btn_dialog_cancel)
                var btnClose = dialog.findViewById<ImageButton>(R.id.btn_dialog_imgcancel)
                var btnSend = dialog.findViewById<Button>(R.id.btn_dialog_send)
                var edGenId = dialog.findViewById<EditText>(R.id.ed_gen_id)

                edGenId.gravity = Gravity.CENTER_HORIZONTAL

                btnSend.setOnClickListener {

                    var genId = edGenId.text.toString()


//                    Toast.makeText(this, "Clicked! clicked! clicked!", Toast.LENGTH_SHORT)
//                        .show()
//                    Log.d("SEND", "Clicked! clicked! clicked!")
//                    Log.d("GEN_ID: ", edGenId.text.toString())

                    if (genId.length == 8) {
                        employee = getDataFromFirebase(edGenId.text.toString())
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this, "Mã nhân viên chưa được nhập đủ!", Toast.LENGTH_SHORT)
                    }
                }

                btnCancel.setOnClickListener {
                    dialog.dismiss()
                }
                btnClose.setOnClickListener {
                    dialog.dismiss()
                }

                count2 = 0
            }
        }
        if (employee.name != "")
            show_greeting_quote.text =
                "Chào bạn " + employee.name + ", chúc bạn một ngày Phụ nữ Việt Nam thật vui và ý nghĩa"

//        //Get Firebase Instance
//        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
////        database.goOffline()
//
//        //todo ok
//        var empReference2: DatabaseReference = database.getReference("employee")
//
//        //todo ok
//        val query = empReference2.orderByChild("genid").equalTo("10592215")
//        query.addChildEventListener(object : ChildEventListener {
//            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                val id = snapshot.key
//
//                val emp = snapshot.getValue(Employee::class.java)
//                Log.d("TOAN22222", emp.toString() + "\n id: $id")
//                //sau khi quay
//                emp?.status = "1"
//                empReference2.child(id!!).setValue(emp)
//            }
//
//            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onChildRemoved(snapshot: DataSnapshot) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//        })
//
//        Log.d("TOAN", "BEGIN")
//        empReference2.addChildEventListener(childEventListener)
    }

    fun getDataFromFirebase(genId: String): Employee {
        var employee: Employee = Employee()
        //Get Firebase Instance
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
//        database.goOffline()

        //todo ok
        var empReference2: DatabaseReference = database.getReference("employee")

        //todo ok
        val query = empReference2.orderByChild("genid").equalTo(genId)
//        val query = empReference2.orderByChild("genid").equalTo("15842289")
        query.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val id = snapshot.key

                val emp = snapshot.getValue(Employee::class.java)
                if (emp != null) {
//                   var map : Map<String, Objects> = emp as Map<String, Objects>
                    var no: String = emp.no
                    var genid: String = emp.genid
                    var name: String = emp.fullname
                    var lastname: String = emp.name
                    var status: String = emp.status
                    employee = Employee(no, genid, name, lastname, status)

                    updateGreetingQuote(employee)
                }



                Log.d("TOAN22222", emp.toString() + "\n id: $id")
                //sau khi quay
                emp?.status = "1"

                empReference2.child(id!!).setValue(emp)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        Log.d("TOAN", "BEGIN")
        empReference2.addChildEventListener(childEventListener)
        return employee
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

    private fun styleForTextView(view: TextView, fontName: String){
//        var typeFace: Typeface = Typeface.createFromAsset(assets, "UTM Androgyne.ttf")
        var typeFace: Typeface = Typeface.createFromAsset(assets, fontName)
        view.typeface = typeFace
    }

    private fun updateGreetingQuote(employee: Employee) {
        if (employee.status.toInt() == 0) btn_spin.visibility = View.VISIBLE
//        var typeFace: Typeface = Typeface.createFromAsset(assets, "UTM Androgyne.ttf")
//        show_greeting_quote.typeface = typeFace
        styleForTextView(show_greeting_quote, "UTM Androgyne.ttf")
        show_greeting_quote.text =
            "Chào bạn \"" + employee.name + "\",\nChúc bạn một ngày Phụ nữ Việt Nam\n thật vui và ý nghĩa"
        show_greeting_quote.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        val sharedPref: SharedPreferences = getSharedPreferences(PREF_INDEX, PRIVATE_MODE)
        caseOfGift = sharedPref.getString(PREF_GIFT, "").toString()
    }

    private fun generateArray(): String {
        val arr = mutableListOf<Int>()
        // tổng số lượt quay
        for (i in 1..10) {
            arr.add(0)
        }

        /// VASERLIN
        for (i in 1..75)
            arr.add(1)
        /// BÔNG TẨY TRANG
        for (i in 1..70)
            arr.add(2)
        /// DAO CAO RÂU
        for (i in 1..60)
            arr.add(3)
        /// BỘ CẮT MÓNG
        for (i in 1..70)
            arr.add(4)
        /// TÚI ĐỰNG
        for (i in 1..110)
            arr.add(5)
        /// BĂNG VẢI CÀI TÓC
        for (i in 1..50)
            arr.add(6)
        /// VỚ
        for (i in 1..60)
            arr.add(7)
        /// CHUỐI
        for (i in 1..10)
            arr.add(8)
        /// DOVE
        for (i in 1..40)
            arr.add(9)

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

        Log.d("ndt", degrees.toString())
        var text = ""
        styleForTextView(tvGiftName, "UTM Androgyne.ttf")

        //0. no gift
        if (degrees == 0f || degrees == 360f) {
            text = "Chúc bạn may mắn lần sau!"
            imGift.setImageResource(R.drawable.mm)
            gim_congras.visibility = View.GONE
            tvGiftName.visibility = View.GONE
            Log.d("ndt", "MM")
        } else {
            gim_congras.visibility = View.VISIBLE
        }
        // 1. vaserlin
        if (degrees == 36f) {
            text = "Dưỡng môi Vaserlin"
            imGift.setImageResource(R.drawable.vaserlin)
            tvGiftName.text = "SEHC thân gửi bạn " + employee.name + " phần quà " + text
            tvGiftName.visibility = View.VISIBLE
            Log.d("ndt", "Vaserlin")
        }

        // 2. Bông tẩy trang
        if (degrees == 72f) {
            text = "Bông tẩy trang"
            imGift.setImageResource(R.drawable.bongtaytrang)
            tvGiftName.text = "SEHC thân gửi bạn " + employee.name + " phần quà " + text
            tvGiftName.visibility = View.VISIBLE
            Log.d("ndt", "Bông tẩy trang")
        }

        // 3. Dao cạo chân mày
        if (degrees == 108f) {
            text = "Dao cạo chân mày"
            imGift.setImageResource(R.drawable.daocao)
            tvGiftName.text = "SEHC thân gửi bạn " + employee.name + " phần quà " + text
            tvGiftName.visibility = View.VISIBLE
            Log.d("ndt", "Dao cạo chân mày")
        }

        // 4. Bộ cắt móng
        if (degrees == 144f) {
            text = "Bộ cắt móng"
            imGift.setImageResource(R.drawable.bocatmong)
            tvGiftName.text = "SEHC thân gửi bạn " + employee.name + " phần quà " + text
            tvGiftName.visibility = View.VISIBLE
            Log.d("ndt", "Bộ cắt móng")
        }

        // 5. Túi đựng mỹ phẩm
        if (degrees == 180f) {
            text = "Túi đựng mỹ phẩm"
            imGift.setImageResource(R.drawable.tuidung)
            tvGiftName.text = "SEHC thân gửi bạn " + employee.name + " phần quà " + text
            tvGiftName.visibility = View.VISIBLE
            Log.d("ndt", "Túi đựng mỹ phẩm")
        }

        // 6. Băng vải cài tóc
        if (degrees == 216f) {
            text = "Băng vải cài tóc"
            imGift.setImageResource(R.drawable.vaserlin)
            tvGiftName.text = "SEHC thân gửi bạn " + employee.name + " phần quà " + text
            tvGiftName.visibility = View.VISIBLE
            Log.d("ndt", "Băng vải cài tóc")
        }

        // 7. Vớ
        if (degrees == 252f) {
            text = "Vớ"
            imGift.setImageResource(R.drawable.vo)
            tvGiftName.text = "SEHC thân gửi bạn " + employee.name + " phần quà " + text
            tvGiftName.visibility = View.VISIBLE
            Log.d("ndt", "Vớ")
        }

        // 8. Chuối
        if (degrees == 288f) {
            text = "Chuối"
            imGift.setImageResource(R.drawable.chuoi)
            tvGiftName.text = "SEHC thân gửi bạn " + employee.name + " phần quà " + text
            tvGiftName.visibility = View.VISIBLE
            Log.d("ndt", "Chuối")
        }

        // 9. Dove
        if (degrees == 324f) {
            text = "Dove"
            imGift.setImageResource(R.drawable.dove)
            tvGiftName.text = "SEHC thân gửi bạn " + employee.name + " phần quà " + text
            tvGiftName.visibility = View.VISIBLE
            Log.d("ndt", "Dove")
        }
        return tvGiftName.text.toString()
    }


}
