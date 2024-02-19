package com.kakusummer.sample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Button
import com.assistant.bases.BaseActivity
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.ActivityMainBinding
import com.kakusummer.sample.service.CountingService

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_main
    override val TAG: String
        get() = "TAG_MainActivity"

    private var countReceiver: BroadcastReceiver? = null
    private lateinit var serviceIntent: Intent
    override fun initView() {
        super.initView()
        binding.apply {

            serviceIntent = Intent(this@MainActivity, CountingService::class.java)



            tvStart.setOnClickListener {
                startService(serviceIntent.setAction(CountingService.ACTION_START))
            }

            tvReset.setOnClickListener {
                startService(serviceIntent.setAction(CountingService.ACTION_RESET))
            }

            countReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    val count = intent?.getIntExtra("count", 0)
                    tvTest.text = count.toString()
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        registerReceiver(countReceiver, IntentFilter(CountingService.COUNT_UPDATE))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(countReceiver)
    }


}