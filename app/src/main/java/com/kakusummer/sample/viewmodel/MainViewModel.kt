package com.dawn.kotlinbasedemo.vm

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.dawn.kotlinbasedemo.http.ApiException
import com.kakusummer.network.api.catchException
import com.kakusummer.network.api.next
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

class MainViewModel : BaseViewModel() {
    val responseText=ObservableField<String>("11111");

    /**
     * 请求网络
     */
    fun requestData(){
        viewModelScope.launch {

            request(false) {
                getMessage()
            }.next {
                Log.e("yeTest","data=====>${this.data}")
                responseText.set(this.data.toString())
            }.catchException {
                Log.d("yeTest", "requestData: "+this)
                when(this){
                    is ApiException->{
                        Log.d("yeTest  ApiException", "requestData: "+this.code)
                        Log.d("yeTest  ApiException", "requestData: "+this.message)
                    }
                    is IOException->{

                    }
                    else->{

                    }
                }
            }
        }
    }

    /**
     * 轮询
     */
    fun requestLoopData(){
        viewModelScope.launch {
            runCatching {
                while (true) {
                    requestSimple {
                        getMessage();
                    }.next {
                        Log.e("yeTest","requestLoopData=====>${this.data}")
                    }
                    delay(1 * 1000)//1s请求一次
                }

            }
        }

    }



}