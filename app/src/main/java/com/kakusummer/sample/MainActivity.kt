package com.kakusummer.sample

import android.app.ProgressDialog
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import com.assistant.bases.BaseActivity
import com.dawn.kotlinbasedemo.vm.MainViewModel
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.ActivityMainBinding
import com.kakusummer.sample.network.api.NetWorkService
import com.kakusummer.sample.network.data.DataBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_main
    override val TAG: String
        get() = "TAG_MainActivity"

    lateinit var viewModel: MainViewModel
    private var loadingDialog:ProgressDialog?=null;
    override fun initView() {
        super.initView()
  /*      var tipUserDialog: TipUserPoliceDialog? = null
        tipUserDialog = TipUserPoliceDialog(this@MainActivity) {
            if (it) {
                tipUserDialog?.dismiss()
            } else {
                finish()
                tipUserDialog?.dismiss()
            }
        }
        tipUserDialog.show()*/
    }


    override fun initDate() {
        super.initDate()
        val BASE_URL = "https://api.github.com/"
        val client = OkHttpClient()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val callRetro=retrofit.create(NetWorkService::class.java)
        callRetro.getDataBean?.enqueue(object : Callback<DataBean?> {
            override fun onResponse(call: Call<DataBean?>, response: Response<DataBean?>) {
                Log.d("yeTest", "call: "+call)
                Log.d("yeTest", "onResponse: "+response)
                Log.d("yeTest", "onResponse body: "+response.body())
            }

            override fun onFailure(call: Call<DataBean?>, t: Throwable) {
                Log.d("yeTest", "onFailure: ")
            }

        })



        lifecycleScope.launch {
           with(Dispatchers.IO){


//               Api.getMessageTest().enqueue(object : Callback<MessageBean?> {
//
//                   override fun onResponse(
//                       call: Call<MessageBean?>,
//                       response: Response<MessageBean?>
//                   ) {
//                       Log.d("yeTest", "onResponse: "+response)
//                   }
//
//                   override fun onFailure(call: Call<MessageBean?>, t: Throwable) {
//                       Log.d("yeTest", "onFailure: "+t)
//                   }
//
//               });
           }
        }


      /*  viewModel = ViewModelProvider(
            this@MainActivity,
            ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]

        viewModel.requestData()
        viewModel.requestLoopData()

        viewModel.loadingEvent.observe(this) {
            if (it){
                showLoading()
            }else{
                dismissLoading()
            }

        }*/
    }


    fun showLoading() {
        if(loadingDialog==null){
            loadingDialog= ProgressDialog(this).apply {
                setMessage("加载中。。。")
            };
        }
        if (!loadingDialog!!.isShowing) {
            loadingDialog!!.show()
        }
    }


    fun dismissLoading() {
        loadingDialog?.dismiss()
    }

    override fun initListener() {
        super.initListener()

        //可以覆盖掉父监听
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("yeTest", "handleOnBackPressed cover: ")
            }
        })
    }
}