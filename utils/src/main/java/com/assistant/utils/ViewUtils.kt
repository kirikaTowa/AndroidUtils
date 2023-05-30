package com.assistant.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes

object ViewUtils {

    fun layoutId2View(@LayoutRes layoutId: Int): View {
        val inflate =
            UtilsBridge.app.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return inflate.inflate(layoutId, null)
    }
}