package com.assistant.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


object BlueAndEarPhoneUtils {
    class HeadphonesReceiver(private var callBack: ((connect: Boolean) -> Unit)) : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.action.equals(Intent.ACTION_HEADSET_PLUG)) {
                val state: Int = intent.getIntExtra("state", -1)
                if (state == 0) {
                    callBack.invoke(false)
                } else if (state == 1) {
                    callBack.invoke(true)
                }
            }
        }
    }
}