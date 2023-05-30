package com.assistant.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View

object ConvertUtils {
    /**
     * Drawable to bitmap.
     */
    fun drawable2Bitmap(drawable: Drawable?): Bitmap {
        return UtilsBridge.drawable2Bitmap(drawable)
    }

    /**
     * Bitmap to drawable.
     */
    fun bitmap2Drawable(bitmap: Bitmap?): Drawable {
        return UtilsBridge.bitmap2Drawable(bitmap)
    }

    /**
     * View to bitmap.
     */
    fun view2Bitmap(view: View?): Bitmap {
        return UtilsBridge.view2Bitmap(view)
    }

    /**
     * Value of dp to value of px.
     */
    fun dp2px(dpValue: Float): Int {
        return UtilsBridge.dp2px(dpValue)
    }

    /**
     * Value of px to value of dp.
     */
    fun px2dp(pxValue: Float): Int {
        return UtilsBridge.px2dp(pxValue)
    }

    /**
     * Value of sp to value of px.
     */
    fun sp2px(spValue: Float): Int {
        return UtilsBridge.sp2px(spValue)
    }

    /**
     * Value of px to value of sp.
     */
    fun px2sp(pxValue: Float): Int {
        return UtilsBridge.px2sp(pxValue)
    }
}