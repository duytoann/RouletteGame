package com.roulettegame.roulettegameapp

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_admin.*
import java.text.DecimalFormat
import java.text.NumberFormat


class AdminActivity : AppCompatActivity() {
    private val PREF_INDEX = "index"
    private var PRIVATE_MODE = 0
    private val PREF_GIFT = "gift"
    private var caseOfGift = ""
    private var totalMoney = 0
    private var totalBT1 = 0
    private var totalBT2 = 0
    private var totalStrongBow = 0
    private var totalCard10k = 0
    private var totalCard20k = 0
    private var totalCard50k = 0
    private var usedMoney = 0
    private var usedBT1 = 0
    private var usedBT2 = 0
    private var usedStrongBow = 0
    private var usedCard10k = 0
    private var usedCard20k = 0
    private var usedCard50k = 0
    private var index = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        //close
        tv_close.setOnClickListener {
            finish()
        }
        //reset
        btn_reset.setOnClickListener {
            showDialog("Xác nhận reset ứng dụng", R.id.btn_reset)
        }
        //add
        btn_add.setOnClickListener {
            showDialog("Xác nhận thêm 250k vào giải thưởng", R.id.btn_add)
        }
        //minus
        btn_minus.setOnClickListener {
            if(caseOfGift.length == 150 || caseOfGift.length-75 <= index)
                Toast.makeText(this,"Không thể giảm phần thưởng",Toast.LENGTH_LONG).show()
            else showDialog("Xác nhận giảm 250k vào giải thưởng", R.id.btn_minus)
        }

    }


    private fun getData(){
        val sharedPref: SharedPreferences = getSharedPreferences(PREF_INDEX, PRIVATE_MODE)
        index = sharedPref.getInt(PREF_INDEX, 0)
        caseOfGift = sharedPref.getString(PREF_GIFT,"").toString()
        // get total gift per 500k
        totalBT1 = getTotal('1')
        totalBT2 = getTotal('2')
        totalStrongBow = getTotal('3')
        totalCard10k = getTotal('4')
        totalCard20k = getTotal('5')
        totalCard50k = getTotal('6')
        totalMoney = 250000*(caseOfGift.length/75)
        // get used
        usedMoney = getUsedGiaiThuong()
        usedBT1 = getUsed('1')
        usedBT2 = getUsed('2')
        usedStrongBow = getUsed('3')
        usedCard10k = getUsed('4')
        usedCard20k = getUsed('5')
        usedCard50k = getUsed('6')

        // format currency
        val formatter: NumberFormat = DecimalFormat("#,###")
        val myNumber = 1000000.0
        val formattedNumber: String = formatter.format(myNumber)
        //set total gift
        tv_sum_giai_thuong.text = formatter.format(totalMoney)
        tv_sum_quay.text = caseOfGift.length.toString()
        tv_sum_1.text = totalBT1.toString()
        tv_sum_2.text = totalBT2.toString()
        tv_sum_3.text = totalStrongBow.toString()
        tv_sum_4.text = totalCard10k.toString()
        tv_sum_5.text = totalCard20k.toString()
        tv_sum_6.text = totalCard50k.toString()
        //set used gift
        tv_used_giai_thuong.text = formatter.format(usedMoney)
        tv_used_quay.text = index.toString()
        tv_used_1.text = usedBT1.toString()
        tv_used_2.text = usedBT2.toString()
        tv_used_3.text = usedStrongBow.toString()
        tv_used_4.text = usedCard10k.toString()
        tv_used_5.text = usedCard20k.toString()
        tv_used_6.text = usedCard50k.toString()
        //set left gift
        tv_left_giai_thuong.text = formatter.format(totalMoney-usedMoney)
        tv_left_quay.text = (caseOfGift.length - index).toString()
        tv_left_1.text = (totalBT1-usedBT1).toString()
        tv_left_2.text = (totalBT2-usedBT2).toString()
        tv_left_3.text = (totalStrongBow-usedStrongBow).toString()
        tv_left_4.text = (totalCard10k-usedCard10k).toString()
        tv_left_5.text = (totalCard20k-usedCard20k).toString()
        tv_left_6.text = (totalCard50k-usedCard50k).toString()
    }


    private fun getUsed(id: Char): Int{
        if(index == 0)
            return 0
        var used = 0
        for(i in 0 until index){
            if(caseOfGift[i] == id)
                used++
        }
        return used
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
                '1' -> used+= 5000
                '2' -> used+= 10000
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

    //show dialog
    private fun showDialog(mess: String, buttonID: Int){
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(this)

        // set message of alert dialog
        dialogBuilder.setMessage(mess)
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("OK") { dialog, _ ->
                run {
                    when(buttonID){
                        R.id.btn_reset -> handleReset()
                        R.id.btn_add -> handleAdd()
                        R.id.btn_minus -> handleMinus()
                    }
                    dialog.dismiss()
                }
            }
            // negative button text and action
            .setNegativeButton("Hủy") { dialog, _ ->
                run {
                    dialog.cancel()
                }
            }

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Confirm")
        // show alert dialog
        alert.show()
    }

    private fun handleReset() {
        val sharedPref: SharedPreferences = getSharedPreferences(PREF_INDEX, PRIVATE_MODE)
        val editor = sharedPref.edit()
        editor.putString(PREF_GIFT, generate500k())
        editor.putInt(PREF_INDEX, 0)
        editor.apply()
        getData()
    }

    private fun handleMinus() {
        val sharedPref: SharedPreferences = getSharedPreferences(PREF_INDEX, PRIVATE_MODE)
        val editor = sharedPref.edit()
        caseOfGift = caseOfGift.removeRange(caseOfGift.length-1-75,caseOfGift.length-1)
        editor.putString(PREF_GIFT, caseOfGift)
        editor.apply()
        getData()
    }

    private fun handleAdd() {
        val sharedPref: SharedPreferences = getSharedPreferences(PREF_INDEX, PRIVATE_MODE)
        val editor = sharedPref.edit()
        editor.putString(PREF_GIFT, caseOfGift+generate250k())
        editor.apply()
        getData()
    }


    private fun generate500k(): String{
        val arr = mutableListOf<Int>()
        for(i in 1..108){
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
    private fun generate250k(): String{
        val arr = mutableListOf<Int>()
        for(i in 1..54){
            arr.add(0)
        }
        for(i in 1..5)
            arr.add(1)
        for(i in 1..3)
            arr.add(2)
        for(i in 1..5)
            arr.add(3)
        for(i in 1..5)
            arr.add(4)
        for(i in 1..2)
            arr.add(5)
        //for(i in 1..1)
            arr.add(6)
        arr.shuffle()
        var result = ""
        for(i in 0 until arr.size)
            result+= arr[i]
        return result
    }
}
