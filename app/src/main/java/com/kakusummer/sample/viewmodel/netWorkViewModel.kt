package com.dawn.kotlinbasedemo.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.assistant.utils.LogUtils
import com.kakusummer.sample.network.data.DataBean
import com.kakusummer.sample.network.entity.RetrofitObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class netWorkViewModel : ViewModel() {
    private val _loadingEvent = MutableLiveData<Boolean>()
    val loadingEvent: LiveData<Boolean>
        get() = _loadingEvent

    private val _dataMessage = MutableLiveData<DataBean>()
    val dataMessage: LiveData<DataBean>
        get() = _dataMessage


    //设置这个中转站
    fun requestData() {
        showLoading()
        RetrofitObject.callRetro.getDataBean?.enqueue(object : Callback<DataBean?> {
            override fun onResponse(call: Call<DataBean?>, response: Response<DataBean?>) {
                LogUtils.d("yeTest", "call: $call")
                LogUtils.d("yeTest", "onResponse: $response")
                LogUtils.d("yeTest", "onResponse body: " + response.body())
                if (response.body()!=null){
                    setDataMessage(response.body()!!)
                }
                closeLoading()
            }

            override fun onFailure(call: Call<DataBean?>, t: Throwable) {
                Log.d("yeTest", "onFailure: ")
                closeLoading()
            }
        });
    }

    /**
     * 轮询
     */
//    fun requestLoopData(){
//        viewModelScope.launch {
//            runCatching {
//                while (true) {
//                    requestSimple {
//                        getMessage();
//                    }.next {
//                        Log.e("yeTest","requestLoopData=====>${this.data}")
//                    }
//                    delay(1 * 1000)//1s请求一次
//                }
//
//            }
//        }
//
//    }

    private fun setDataMessage(dataBean:DataBean){
        _dataMessage.value=dataBean
    }

    private fun showLoading() {
        _loadingEvent.value = true
    }

    private fun closeLoading() {
        _loadingEvent.value = false
    }
}