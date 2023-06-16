package com.kakusummer.sample

import com.assistant.bases.BaseActivity
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.ActivityMainBinding
import com.kakusummer.sample.Dialog.TipUserPoliceDialog

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_main
    override val TAG: String
        get() = "TAG_MainActivity"

    override fun initView() {
        super.initView()
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