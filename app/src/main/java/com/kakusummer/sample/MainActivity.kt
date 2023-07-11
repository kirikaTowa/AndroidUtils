package com.kakusummer.sample

import android.app.ProgressDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.assistant.bases.BaseActivity
import com.kakusummer.sample.viewmodel.netWorkViewModel
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.ActivityMainBinding
import kotlinx.coroutines.launch


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
        //虽然viewModel里执行的是异步请求，外层使用lifecycleScope.launch感知生命周期
        lifecycleScope.launch {
            viewModel.requestData()
        }
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