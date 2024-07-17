package com.kakusummer.sample

import android.content.res.Resources
import android.graphics.Color
import android.util.TypedValue

object CommonUtils {

    fun String.parseColor(): Int = Color.parseColor(this)

    val Int.dp
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this.toFloat(),
            Resources.getSystem().displayMetrics)
}