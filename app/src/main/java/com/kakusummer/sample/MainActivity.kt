package com.kakusummer.sample

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
        checkBluetoothPermission()
        registerEarPhoneReceiver()
    }



    val REQUEST_BLUETOOTH_PERMISSION = 1

    // 在需要检查和请求蓝牙权限的地方调用此方法
    private fun checkBluetoothPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH
            ) === PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_ADMIN
            ) === PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) === PackageManager.PERMISSION_GRANTED
        ) {
            // 已经有了蓝牙权限，可以执行您的蓝牙相关操作
        } else {
            // 没有蓝牙权限，需要请求权限
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH_CONNECT
                ),
                REQUEST_BLUETOOTH_PERMISSION
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            // 检查权限请求的结果
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用户授予了蓝牙权限，可以执行您的蓝牙相关操作
            } else {
                // 用户拒绝了蓝牙权限，您可以选择相应地处理此情况
            }
        }
    }

    private fun registerEarPhoneReceiver() {
        receiverEar = BlueAndEarPhoneUtils.HeadphonesReceiver {
            if (it) {
                Log.d("yeTest", "耳机 当前连接: ")
            }else{
                Log.d("yeTest", "耳机 当前拔出: ")
            }
        }

        IntentFilter().let {
            it.addAction(Intent.ACTION_HEADSET_PLUG)
            it.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            //ACL 所有蓝牙设备
            it.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            it.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
            registerReceiver(receiverEar, it)
        }



    }

    override fun onDestroy() {
        unregisterReceiver(receiverEar)
        super.onDestroy()
    }
}