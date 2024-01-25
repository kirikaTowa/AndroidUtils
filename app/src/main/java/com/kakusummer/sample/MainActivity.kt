package com.kakusummer.sample

import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.assistant.bases.BaseActivity
import com.assistant.utils.BlueAndEarPhoneUtils
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_main
    override val TAG: String
        get() = "TAG_MainActivity"

    private var receiverEar: BlueAndEarPhoneUtils.HeadphonesReceiver? = null

    override fun initView() {
        super.initView()
    }

    override fun initListener() {
        super.initListener()
        registerEarPhoneReceiver()
    }

    private fun registerEarPhoneReceiver() {
        receiverEar = BlueAndEarPhoneUtils.HeadphonesReceiver {
            if (it) {
                Log.d("yeTest", "耳机 当前连接: ")
            }else{
                Log.d("yeTest", "耳机 当前拔出: ")
            }
        }
        val filter = IntentFilter(Intent.ACTION_HEADSET_PLUG)
        registerReceiver(receiverEar, filter)
    }

    override fun onDestroy() {
        unregisterReceiver(receiverEar)
        super.onDestroy()
    }
}