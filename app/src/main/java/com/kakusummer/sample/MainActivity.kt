package com.kakusummer.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kakusummer.androidutils.R
import com.kakusummer.sample.Dialog.TipUserPoliceDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var tipUserDialog:TipUserPoliceDialog?=null
        tipUserDialog= TipUserPoliceDialog(this@MainActivity) {
            if (it) {
                tipUserDialog?.dismiss()
            } else {
                finish()
                tipUserDialog?.dismiss()
            }
        }
        tipUserDialog.show()
    }

}