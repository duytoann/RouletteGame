package com.roulettegame.roulettegameapp

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_admin_second.*

class AdminActivity2 : AppCompatActivity() {

    private val PREF_INDEX = "index"
    private var PRIVATE_MODE = 0
    private val PREF_GIFT = "gift"
    private var caseOfGift = ""
    private var index = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_second)

        //CLOSE
        tv_close_2.setOnClickListener {
            finish()
        }
    }

    private fun getTotal(id: Char): Int{
        var total = 0
        for(element in caseOfGift){
            if(element == id)
                total++
        }
        return total
    }

    private fun getUsedGiaiThuong(): Int{
        if(index == 0)
            return 0
        var used = 0
        for(i in 0 until index){
            when(caseOfGift[i]){
                '0' -> used+= 0
                '1' -> used+= 1
                '2' -> used+= 1
                '3' -> used+= 10000
                '4' -> used+= 10000
                '5' -> used+= 20000
                '6' -> used+= 50000
            }
        }
        return used
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun getData(){
        val sharedPref: SharedPreferences = getSharedPreferences(PREF_INDEX, PRIVATE_MODE)
        index = sharedPref.getInt(PREF_INDEX, 0)
        caseOfGift = sharedPref.getString(PREF_GIFT,"").toString()

        /// TOTAL GIFT

        /// USED

        /// FORMAT CURRENCY

    }
}
