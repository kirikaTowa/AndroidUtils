package com.assistant.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

object ResourceUtils {

    fun getDrawable(context:Context, @DrawableRes id: Int){
        ContextCompat.getDrawable(context, id)
    }

    fun getColor(context:Context, @ColorRes id: Int){
        ContextCompat.getColor(context, id)
    }
}