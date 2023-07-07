package com.kakusummer.sample

import android.text.TextUtils
import com.assistant.bases.BaseActivity
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_main
    override val TAG: String
        get() = "TAG_MainActivity"

    override fun initView() {
        super.initView()

    }

    override fun initListener() {
        super.initListener()
        val main = intent.getStringExtra("main")
        if (!TextUtils.isEmpty(main)) {
            binding.tvTest.text = main
        }
    }
}