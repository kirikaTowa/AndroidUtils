package com.kakusummer.sample

import android.util.Log
import androidx.activity.OnBackPressedCallback
import com.assistant.bases.BaseActivity
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.ActivityMainBinding
import com.kakusummer.sample.dialog.TipUserPoliceDialog

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_main
    override val TAG: String
        get() = "TAG_MainActivity"

    override fun initView() {
        super.initView()
        binding.also {

        }
        var tipUserDialog:TipUserPoliceDialog?=null
        tipUserDialog= TipUserPoliceDialog(this@MainActivity) {
            if (it) {
                tipUserDialog?.dismiss()
            } else {
                finish()
                tipUserDialog?.dismiss()
            }
        }
        //tipUserDialog.show()
    }

    override fun initListener() {
        super.initListener()
//        //可以覆盖掉父监听
//        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                Log.d("yeTest", "handleOnBackPressed cover: ")
//            }
//        })
    }
}