package com.roulettegame.roulettegameapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_admin.*

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        //close
        tv_close.setOnClickListener {
            finish()
        }
        //add
        btn_add.setOnClickListener {
            showDialog()
        }
        //minus
        btn_minus.setOnClickListener {
            showDialog()
        }
        //reset
        btn_reset.setOnClickListener {
            showDialog()
        }
    }

    //show dialog
    private fun showDialog(){
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(this)

        // set message of alert dialog
        dialogBuilder.setMessage("Are you sure?")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("OK") { dialog, _ ->
                run {
                    handleAction()
                    dialog.dismiss()
                }
            }
            // negative button text and action
            .setNegativeButton("Cancel") { dialog, _ ->
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

    //
    private fun handleAction(){
        Log.d("uytai", "Success")
    }
}
