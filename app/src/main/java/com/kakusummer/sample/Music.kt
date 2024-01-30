package com.kakusummer.sample

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Music(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("album")
    var album: Int? = 0

): Parcelable{
    //如果有需要override 的属性不太好序列化就可以写在这里
    @IgnoredOnParcel
     val musicId
        get() = if (id < 0) 0 else id
}
