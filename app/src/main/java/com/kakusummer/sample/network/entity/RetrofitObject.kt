package com.kakusummer.sample.network.entity

import com.kakusummer.androidutils.BuildConfig
import com.kakusummer.sample.network.api.NetWorkService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitObject{
    val callRetro: NetWorkService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient())
            .build().create(NetWorkService::class.java)
    }

    private fun getOkHttpClient(): OkHttpClient {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS) //设置读取超时时间
            .writeTimeout(30, TimeUnit.SECONDS) //设置写的超时时间
            .connectTimeout(30, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            builder.addInterceptor(httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            })
        }
        return builder.build()
    }
}

