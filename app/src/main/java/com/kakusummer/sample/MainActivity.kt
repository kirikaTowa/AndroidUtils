package com.kakusummer.sample

import android.app.ProgressDialog
import androidx.lifecycle.ViewModelProvider
import com.assistant.bases.BaseActivity
import com.dawn.kotlinbasedemo.vm.netWorkViewModel
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.ActivityMainBinding


class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_main
    override val TAG: String
        get() = "TAG_MainActivity"

    lateinit var viewModel: netWorkViewModel
    private var loadingDialog: ProgressDialog? = null;
    override fun initView() {
        super.initView()
    }


    override fun initDate() {
        super.initDate()

        viewModel = ViewModelProvider(
            this@MainActivity,
            ViewModelProvider.NewInstanceFactory()
        )[netWorkViewModel::class.java]

        viewModel.requestData()
    }

    override fun initListener() {
        viewModel.loadingEvent.observe(this) {
            if (it) {
                showLoading()
            } else {
                dismissLoading()
            }
        }

        viewModel.dataMessage.observe(this) {
            binding.tvMessage.text = it.toString()
        }
    }

    private fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = ProgressDialog(this).apply {
                setMessage("加载中。。。")
            };
        }
        if (!loadingDialog!!.isShowing) {
            loadingDialog!!.show()
        }
    }


    private fun dismissLoading() {
        loadingDialog?.dismiss()
    }
}