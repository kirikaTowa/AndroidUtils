package com.kakusummer.sample

import android.content.Intent
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


            val intent = Intent()
            intent.setClassName("com.kakusummer.androidutils", "com.kakusummer.sample.MainActivity2")
            startActivity(intent)
            //startActivity(intent)
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