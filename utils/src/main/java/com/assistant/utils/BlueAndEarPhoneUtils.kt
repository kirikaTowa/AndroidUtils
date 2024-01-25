package com.assistant.utils

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


object BlueAndEarPhoneUtils {
    class HeadphonesReceiver(private var callBack: ((connect: Boolean) -> Unit)) : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val action = intent.action
            if (action != null) {
                when (action) {
                    //无需任何权限
                    BluetoothAdapter.ACTION_STATE_CHANGED -> {
                        val state =
                            intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                        if (state == BluetoothAdapter.STATE_ON) {
                            Log.d("yeTest", "Bluetooth is turned on: ")
                            // Bluetooth is turned on
                        } else if (state == BluetoothAdapter.STATE_OFF) {
                            Log.d("yeTest", "Bluetooth is turned off: ")
                        }
                    }

                    //设备连接与断开连接 需要动态声明权限
                    BluetoothDevice.ACTION_ACL_CONNECTED -> {
                        Log.d("yeTest", "ACTION_ACL_CONNECTED: ")
                    }
                    BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                        Log.d("yeTest", "ACTION_ACL_DISCONNECTED: ")
                    }

                    //耳机插拔|无需任何权限
                    Intent.ACTION_HEADSET_PLUG->{
                        val state: Int = intent.getIntExtra("state", -1)
                        if (state == 1) {
                            callBack.invoke(true)
                        } else if (state == 0) {
                            callBack.invoke(false)
                        }
                    }
                }
            }
        }
    }




}

