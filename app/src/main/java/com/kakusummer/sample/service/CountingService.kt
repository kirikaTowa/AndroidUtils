package com.kakusummer.sample.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper

class CountingService : Service() {


    private var count = 0
    private var handler: Handler? = Looper.myLooper()?.let { Handler(it) }
    private lateinit var countRunnable: Runnable

    companion object {
        const val ACTION_START = "START"
        const val ACTION_STOP = "STOP"
        const val ACTION_RESET = "RESET"
        const val COUNT_UPDATE = "COUNT_UPDATE"
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startCounting()
            ACTION_STOP -> stopCounting()
            ACTION_RESET -> resetCount()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startCounting() {
        countRunnable = object : Runnable {
            override fun run() {
                count++
                sendBroadcast(Intent(COUNT_UPDATE).putExtra("count", count))
                handler?.postDelayed(this, 1000)
            }
        }
        handler?.post(countRunnable)
    }

    private fun stopCounting() {
        handler?.removeCallbacks(countRunnable)
    }

    private fun resetCount() {
        count = 0
        sendBroadcast(Intent(COUNT_UPDATE).putExtra("count", count))
    }

    override fun onDestroy() {
        super.onDestroy()
        handler?.removeCallbacks(countRunnable)
    }
}