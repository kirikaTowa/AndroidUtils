package com.kakusummer.sample

import com.assistant.bases.BaseActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_main
    override val TAG: String
        get() = "TAG_MainActivity"

    override fun initView() {
        super.initView()
        binding.apply {
            /*tvHello.setOnClickListener {
                pgsBar.setProgress(0.7F)
            }

            val progressDialog = ProgressHomeDialog(this@MainActivity,
                callbackWidth = {
                },
                callbackDepth = {
                })
            progressDialog.show()*/



//            //测试Glide placeholder
//            var options: RequestOptions = RequestOptions()
//                .timeout(30000)
//                .priority(Priority.HIGH)
//
//                options = options.placeholder(getDrawable(R.drawable.recommend_opera_end_placeholder))
//
//            Glide.with(this@MainActivity)
//                .load("")
//                .apply(options)
//                .into(binding.tvTest)
        }



    }

    override fun initListener() {
        super.initListener()
//        //可以覆盖掉父监听
//        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                Log.d("yeTest", "handleOnBackPressed cover: ")
//            }
//        })
    }
}