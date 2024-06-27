package com.kakusummer.sample

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.util.Log
import com.assistant.bases.BaseActivity
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.ActivityRulerViewCustomBinding
import com.kakusummer.sample.dialog.rulerview.RulerWeightDialog


class RulerViewCustomActivity : BaseActivity<ActivityRulerViewCustomBinding>() {



    override val layoutId: Int
        get() = R.layout.activity_ruler_view_custom
    override val TAG: String
        get() = "TAG_VIEW_STUB"

    override fun initDate() {
        super.initDate()
    }

    override fun initView() {
        super.initView()
        //不在fa范围内 刻度尺不会显示
        val rulerView = RulerWeightDialog(this@RulerViewCustomActivity,1,25F) {

        }
        rulerView.show()
    }

    override fun initListener() {
        super.initListener()

        val rulerView = RulerWeightDialog(this@RulerViewCustomActivity,1,25F) {

        }
        rulerView.show()

    }



}