package com.kakusummer.network.api

import android.util.Log

class BaseResponse<T> {
    val data: T? = null;
    val errorCode: Int? = null;
    val errorMsg: String? = null;
    var exception: Exception? = null;
}

inline fun <T> BaseResponse<T>.next(bloc: BaseResponse<T>.() -> Unit): BaseResponse<T> {
    return if (exception == null) {//没有异常，则把正确结果bloc出去
        Log.d("yeTest", "next: ")
        bloc()
        this
    } else {//出现异常（网络/服务器/自定义异常）执行这里 不用bloc
        Log.d("yeTest", "next:1 ")
        this;
    }

}


inline fun <T> BaseResponse<T>.catchException(bloc: Exception.() -> Unit) {
    Log.d("yeTest", "next:2 "+exception)
    if (exception != null) {

        bloc(exception!!)
    }
}