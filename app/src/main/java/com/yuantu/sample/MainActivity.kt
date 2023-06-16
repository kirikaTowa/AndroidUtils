package com.yuantu.sample

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.assistant.bases.BaseActivity
import com.permissionx.guolindev.PermissionX
import com.yuantu.bluetools.R
import com.yuantu.bluetools.databinding.ActivityMainBinding


class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_main
    override val TAG: String
        get() = "TAG_MainActivity"


    private val receiver = object : BroadcastReceiver() {

        @RequiresApi(Build.VERSION_CODES.R)
        @SuppressLint("MissingPermission")
        //不用  Manifest.permission.BLUETOOTH_CONNECT校验权限  API31才有的  完成权限申请后才发起
        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action
            when(action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device?.name
                    val deviceHardwareAddress = device?.address // MAC address\
                    Log.d(TAG, "onReceive device: "+device.toString())
                    Log.d(TAG, "onReceive deviceName: "+deviceName)
                    Log.d(TAG, "onReceive deviceHardwareAddress: "+deviceHardwareAddress)
                    Log.d(TAG, "onReceive deviceHardwareAddress: "+device?.alias)
                    Log.d(TAG, "onReceive 类型: "+device?.type)
                    Log.d(TAG, "onReceive 类型 : "+device?.uuids) //这个不太可靠 可能扫不到
                }
            }
        }
    }


//    val DEVICE_TYPE_UNKNOWN = 0 未知类型
//    val DEVICE_TYPE_CLASSIC = 1 传统类型
//    val DEVICE_TYPE_LE = 2 ble类型
//    val DEVICE_TYPE_DUAL = 3 传统和ble双重类型


    override fun initView() {
        super.initView()
    }

    override fun initDate() {
        super.initDate()
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)
        checkBlueTooth()
    }

    @SuppressLint("MissingPermission")
    private fun checkBlueTooth(){
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            Toast.makeText(this,getString(R.string.str_no_blue_tooth), Toast.LENGTH_SHORT).show()
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            PermissionX.init(this@MainActivity)
                .permissions(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN,Manifest.permission.ACCESS_COARSE_LOCATION)
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        Toast.makeText(this, "所需蓝牙权限已通过", Toast.LENGTH_LONG).show()
                        if (!bluetoothAdapter.isEnabled) {
                            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                            startActivityForResult(enableBtIntent, Constant.REQUEST_ENABLE_BT)
                            bluetoothAdapter.startDiscovery()
                            Log.d(TAG, "startDiscovery()")
                        }
                    } else {
                        Toast.makeText(this, "These permissions are denied: $deniedList", Toast.LENGTH_LONG).show()
                    }
                }
        }
        bluetoothAdapter.startDiscovery()
        Log.d(TAG, "startDiscovery(x)")
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }


}