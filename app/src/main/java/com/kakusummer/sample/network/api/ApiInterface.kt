package com.kakusummer.network.api

import retrofit2.Call
import retrofit2.http.GET


interface ApiInterface {
    @GET("api/rand.qinghua?format=json")
    suspend fun getMessage(): BaseResponse<MessageBean?>


    @GET("api/rand.qinghua?format=json")
    suspend fun getMessageTest(): Call<MessageBean?>
}