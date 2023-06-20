package com.kakusummer.sample.network.api

import com.kakusummer.sample.network.data.DataBean
import retrofit2.Call
import retrofit2.http.GET


interface NetWorkService {
    @get:GET("users/kirikaTowa")
    val getDataBean: Call<DataBean?>?
}
