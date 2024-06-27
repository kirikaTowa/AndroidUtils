package com.kakusummer.sample

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.util.Log
import com.assistant.bases.BaseActivity
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.ActivityTorchBinding
import com.kakusummer.androidutils.databinding.ActivityWheelViewCustomBinding


class WheelViewCustomActivity : BaseActivity<ActivityWheelViewCustomBinding>() {

    private var cameraManager: CameraManager? = null
    private var cameraId: String? = null

    private var TorchState:Boolean=false

    override val layoutId: Int
        get() = R.layout.activity_torch
    override val TAG: String
        get() = "TAG_VIEW_STUB"

    override fun initDate() {
        super.initDate()
        judgeFlashLight()
    }

    override fun initView() {
        super.initView()
    }

    override fun initListener() {
        super.initListener()
        binding.ivTorch.setOnClickListener {
            if (TorchState){
                TorchState=false
                turnOffFlashLight()
            }else{
                TorchState=true
                turnOnFlashLight()
            }
        }
    }

    //判断闪光灯权限
    private fun judgeFlashLight() {
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            // 获取相机设备列表
            val cameraIdList = cameraManager!!.cameraIdList
            if (cameraIdList.isEmpty()) {
                Log.d("yeTest", "judgeFlashLight: NO DEVICE")
                // 如果没有相机设备，则该设备不支持手电筒功能
                // 处理该情况，比如显示一条消息告诉用户设备不支持手电筒
            } else {
                cameraId = cameraIdList[0] // 默认使用列表中第一个相机设备
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
            // 处理异常情况
            Log.d("yeTest", "judgeFlashLight: ERROR")
        }
    }

    // 控制手电筒打开
    private fun turnOnFlashLight() {
        Log.d("yeTest", "turnOnFlashLight: ")
        try {
            if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                cameraManager?.setTorchMode(cameraId!!, true)
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
            // 处理异常情况
            Log.d("yeTest", "turnOnFlashLight: Error")
        }
    }

    // 控制手电筒关闭
    private fun turnOffFlashLight() {
        Log.d("yeTest", "turnOffFlashLight: ")
        try {
            if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                cameraManager?.setTorchMode(cameraId!!, false)
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
            // 处理异常情况
            Log.d("yeTest", "turnOffFlashLight: ERROR")
        }
    }


}