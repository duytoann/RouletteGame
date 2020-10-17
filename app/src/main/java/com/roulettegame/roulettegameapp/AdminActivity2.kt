package com.roulettegame.roulettegameapp

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_admin_second.*

class AdminActivity2 : AppCompatActivity() {

    private val PREF_INDEX = "index"
    private var PRIVATE_MODE = 0
    private val PREF_GIFT = "gift"
    private var caseOfGift = ""
    private var index = 0

    // TỔNG SỐ PHẦN QUÀ
    private var totalOfGift = 0
    private var totalOfVaserlin = 0
    private var totalOfBongTayTrang = 0
    private var totalOfDaoCaoChanMay = 0
    private var totalOfBoCatMong = 0
    private var totalOfTuiDung = 0
    private var totalOfBangVaiCaiToc = 0
    private var totalOfVo = 0
    private var totalOfChuoi = 0
    private var totalOfDove = 0

    // PHẦN QUÀ ĐÃ SỬ DỤNG
    private var usedOfGift = 0
    private var usedOfVaserlin = 0
    private var usedOfBongTayTrang = 0
    private var usedOfDaoCaoChanMay = 0
    private var usedOfBoCatMong = 0
    private var usedOfTuiDung = 0
    private var usedOfBangVaiCaiToc = 0
    private var usedOfVo = 0
    private var usedOfChuoi = 0
    private var usedOfDove = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_second)

        //CLOSE
        tv_close_2.setOnClickListener {
            finish()
        }

        // RESET
        tv_reset_2.setOnClickListener {
            showDialog("Xác nhận thiết lập lại dữ liệu ban đầu!", R.id.tv_reset_2)
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

    private fun getUsedGiaiThuong(): Int{
        if(index == 0)
            return 0
        var used = 0
        for(i in 0 until index){
            when(caseOfGift[i]){
                '0' -> used+= 0
                '1' -> used+= 1
                '2' -> used+= 1
                '3' -> used+= 1
                '4' -> used+= 1
                '5' -> used+= 1
                '6' -> used+= 1
                '7' -> used+= 1
                '8' -> used+= 1
                '9' -> used+= 1
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
        totalOfVaserlin = getTotal('1')
        totalOfBongTayTrang = getTotal('2')
        totalOfDaoCaoChanMay = getTotal('3')
        totalOfBoCatMong = getTotal('4')
        totalOfTuiDung = getTotal('5')
        totalOfBangVaiCaiToc = getTotal('6')
        totalOfVo = getTotal('7')
        totalOfChuoi = getTotal('8')
        totalOfDove = getTotal('9')

        /// USED
        usedOfVaserlin = getUsed('1')
        usedOfBongTayTrang = getUsed('2')
        usedOfDaoCaoChanMay = getUsed('3')
        usedOfBoCatMong = getUsed('4')
        usedOfTuiDung = getUsed('5')
        usedOfBangVaiCaiToc = getUsed('6')
        usedOfVo = getUsed('7')
        usedOfChuoi = getUsed('8')
        usedOfDove = getUsed('9')

        // SET TOTAL
        tv_sum_quay_2.text = caseOfGift.length.toString()
        tv_sum_1.text = totalOfVaserlin.toString()
        tv_sum_2.text = totalOfBongTayTrang.toString()
        tv_sum_3.text = totalOfDaoCaoChanMay.toString()
        tv_sum_4.text = totalOfBoCatMong.toString()
        tv_sum_5.text = totalOfTuiDung.toString()
        tv_sum_6.text = totalOfBangVaiCaiToc.toString()
        tv_sum_7.text = totalOfVo.toString()
        tv_sum_8.text = totalOfChuoi.toString()
        tv_sum_9.text = totalOfDove.toString()

        // SET USED
        tv_used_1.text = usedOfVaserlin.toString()
        tv_used_2.text = usedOfBongTayTrang.toString()
        tv_used_3.text = usedOfDaoCaoChanMay.toString()
        tv_used_4.text = usedOfBoCatMong.toString()
        tv_used_5.text = usedOfTuiDung.toString()
        tv_used_6.text = usedOfBangVaiCaiToc.toString()
        tv_used_7.text = usedOfVo.toString()
        tv_used_8.text = usedOfChuoi.toString()
        tv_used_9.text = usedOfDove.toString()

        // SET LEFT
        tv_left_1.text = (totalOfVaserlin -usedOfVaserlin).toString()
        tv_left_2.text = (totalOfBongTayTrang - usedOfBongTayTrang).toString()
        tv_left_3.text = (totalOfDaoCaoChanMay -usedOfDaoCaoChanMay).toString()
        tv_left_4.text = (totalOfBoCatMong - usedOfBoCatMong).toString()
        tv_left_5.text = (totalOfTuiDung - usedOfTuiDung).toString()
        tv_left_6.text = (totalOfBangVaiCaiToc - usedOfBangVaiCaiToc).toString()
        tv_left_7.text = (totalOfVo - usedOfVo).toString()
        tv_left_8.text = (totalOfChuoi - usedOfChuoi).toString()
        tv_left_9.text = (totalOfDove - usedOfDove).toString()


    }

    private fun handleReset(){
        val sharedPref: SharedPreferences = getSharedPreferences(PREF_INDEX, PRIVATE_MODE)
        val editor = sharedPref.edit()
        editor.putString(PREF_GIFT, generateAmountOfGift())
        editor.putInt(PREF_INDEX, 0)
        editor.apply()
        getData()
    }
    private fun generateAmountOfGift() : String{
        val arr = mutableListOf<Int>()
        // TỔNG SỐ LƯỢT QUAY
        for (i in 1..872) {
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

    //show dialog
    private fun showDialog(mess: String, buttonID: Int){
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(this)

        // set message of alert dialog
        dialogBuilder.setMessage(mess)
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Áp dụng") { dialog, _ ->
                run {
                    when(buttonID){
                        R.id.tv_reset_2 -> handleReset()
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
}
