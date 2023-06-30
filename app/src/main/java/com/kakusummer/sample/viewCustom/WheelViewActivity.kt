package com.kakusummer.sample.viewCustom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.assistant.bases.BaseActivity
import com.assistant.viewcustom.horizontalwheelview.HorizontalWheelView
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.ActivityWheelViewBinding

class WheelViewActivity : BaseActivity<ActivityWheelViewBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_wheel_view
    override val TAG: String
        get() = "TAG_WheelViewActivity"

    override fun initView() {
        super.initView()
    }

    override fun initListener() {
        super.initListener()
        binding.horizontalWheelView.setListener(object: HorizontalWheelView.Listener(){
            override fun onRotationChanged(radians: Double) {
                super.onRotationChanged(radians)
                Log.d(TAG, "onRotationChanged: "+radians)
            }
        }
        )
    }
}